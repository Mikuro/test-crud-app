package com.pushpyshev.service

import com.pushpyshev.exception.internalException
import com.pushpyshev.model.dto.counter.CounterData
import com.pushpyshev.model.dto.counter.create.CreateCounterRequest
import com.pushpyshev.repository.CounterRepository
import org.slf4j.LoggerFactory

class CounterService(private val counterRepository: CounterRepository) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    suspend fun readCounter(counterName: String): Int {
        logger.info("Searching counter with name = $counterName")
        val counter = counterRepository.findCounterByName(counterName) ?: run {
            logger.error("Counter with name = $counterName not found")
            throw internalException("LrZtdG")
        }
        return counter.value
    }

    suspend fun createCounter(createCounterRequest: CreateCounterRequest) {
        logger.info("Creating counter with name = ${createCounterRequest.name}  and base value = ${createCounterRequest.value}")
        val newCounter = counterRepository.checkExistsAndSave(createCounterRequest.name, createCounterRequest.value)
        if (newCounter == null) {
            logger.info("Counter with name '${createCounterRequest.name}' already exists")
        } else {
            logger.info("Created new counter with name '${newCounter.name}' and initial count ${newCounter.value}")
        }
    }

    suspend fun incrementCounter(name : String) : Int {
        logger.info("Incrementing counter with name = $name")
        val newValue = counterRepository.checkExistsAndIncrement(name)
        logger.info("Incremented counter with name = $name and new value = $newValue")
        return newValue
    }

     suspend fun deleteCounter(name : String) {
        logger.info("Deleting counter with name = $name")
        counterRepository.deleteIfExist(name)
        logger.info("Counter with name = $name deleted")
    }

    suspend fun getAllCounters(limit : Int) : List<CounterData> {
        logger.info("Getting all counters")
        return counterRepository.findAll(limit)
    }
}