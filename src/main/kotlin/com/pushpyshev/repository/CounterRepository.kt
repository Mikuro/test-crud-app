package com.pushpyshev.repository

import com.pushpyshev.exception.internalException
import com.pushpyshev.model.dto.counter.CounterData
import com.pushpyshev.model.entity.tables.Counters
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.LoggerFactory

/**
 * This class provides DTOs as result of database queries
 */
class CounterRepository {
    private val logger = LoggerFactory.getLogger(this::class.java)

    fun findCounterByName(name: String): CounterData? = transaction {
        Counters.selectAll().where { Counters.name eq name }
            .mapNotNull { toCounterData(it) }
            .singleOrNull()
    }

    fun checkExistsAndSave(counterName: String, counterValue: Int): CounterData? = transaction {
        val existingCounter = Counters.selectAll().where { Counters.name eq counterName }
            .mapNotNull { toCounterData(it) }
            .singleOrNull()
        if (existingCounter == null) {
            Counters.insertAndGetId {
                it[name] = counterName
                it[value] = counterValue
            }.value
            CounterData(counterName, counterValue)
        }
        null
    }

    fun checkExistsAndIncrement(counterName: String): Int = transaction {
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

    fun deleteIfExist(counterName: String) = transaction {
        val deletedRows = Counters.deleteWhere { name eq counterName }
        logger.info("Deleted $deletedRows rows")
        if (deletedRows == 0) {
            throw internalException("EDUWoG")
        }
    }

    fun findAll(limit: Int): List<CounterData> = transaction {
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

    private fun toCounterData(row: ResultRow): CounterData =
        CounterData(
            name = row[Counters.name],
            value = row[Counters.value]
        )
}