package com.pushpyshev.repository

import com.pushpyshev.exception.internalException
import com.pushpyshev.model.dto.counter.CounterData
import com.pushpyshev.model.entity.tables.Counters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.LoggerFactory

/**
 * This class provides DTOs as result of database queries
 */
class CounterRepository {
    private val logger = LoggerFactory.getLogger(this::class.java)

    suspend fun findCounterByName(name: String): CounterData? = withContext(Dispatchers.IO) {
        transaction {
            Counters.selectAll().where { Counters.name eq name }
                .mapNotNull { toCounterData(it) }
                .singleOrNull()
        }
    }

    suspend fun checkExistsAndSave(counterName: String, counterValue: Int): CounterData? = withContext(Dispatchers.IO) {
        transaction {
            val existingCounter = Counters.selectAll().where { Counters.name eq counterName }
                .mapNotNull { toCounterData(it) }
                .singleOrNull()
            if (existingCounter == null) {
                Counters.insertAndGetId {
                    it[name] = counterName
                    it[value] = counterValue
                }.value
                return@transaction CounterData(counterName, counterValue)
            }
            return@transaction null
        }
    }

    suspend fun checkExistsAndIncrement(counterName: String): Int = withContext(Dispatchers.IO) {
        transaction {
            val result = Counters.select(Counters.value)
                .where { Counters.name eq counterName }
                .forUpdate()
                .map { row ->
                    val updatedValue = row[Counters.value] + 1
                    Counters.update({ Counters.name eq counterName }) {
                        it[value] = updatedValue
                    }
                    updatedValue
                }
                .singleOrNull()
            result ?: throw internalException("XtUFVR")
        }
    }

    suspend fun deleteIfExist(counterName: String) = withContext(Dispatchers.IO) {
        transaction {
            val deletedRows = Counters.deleteWhere { name eq counterName }
            logger.info("Deleted $deletedRows rows")
            if (deletedRows == 0) {
                throw internalException("EDUWoG")
            }
        }
    }

    suspend fun findAll(limit: Int): List<CounterData> = withContext(Dispatchers.IO) {
        transaction {
            val result = Counters.selectAll()
                .mapNotNull { toCounterData(it) }
            if (limit > 0) {
                logger.info("Collected all counters. Current limit $limit")
                result.take(limit)
            } else {
                logger.warn("Collected all counters. Dangerous reading without limit")
                result
            }
        }
    }

    private fun toCounterData(row: ResultRow): CounterData =
        CounterData(
            name = row[Counters.name],
            value = row[Counters.value]
        )
}