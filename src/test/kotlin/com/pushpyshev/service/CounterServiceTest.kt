package com.pushpyshev.service

import com.pushpyshev.exception.AppException
import com.pushpyshev.model.dto.counter.CounterData
import com.pushpyshev.model.dto.counter.create.CreateCounterRequest
import com.pushpyshev.repository.CounterRepository
import org.junit.Assert.assertThrows
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.kotlin.any
import org.mockito.kotlin.verify
import kotlin.test.Test
import kotlin.test.assertEquals

class CounterServiceTest {
    private val counterRepository = mock(CounterRepository::class.java)
    private val counterService = CounterService(counterRepository)

    @Test
    fun test_read_existing_counter() {
        val counterName = "testCounter"
        val counterData = CounterData(counterName, 10)
        `when`(counterRepository.findCounterByName(counterName)).thenReturn(counterData)

        val result = counterService.readCounter(counterName)

        assertEquals(10, result)
        verify(counterRepository).findCounterByName(counterName)
    }

    @Test
    fun test_replicate_read_nonexistent_counter() {
        val counterName = "nonexistentCounter"

        `when`(counterRepository.findCounterByName(counterName)).thenReturn(null)

        val exception = assertThrows(AppException::class.java) {
            counterService.readCounter(counterName)
        }
        assertEquals("ER002", exception.code)
        verify(counterRepository).findCounterByName(any())
    }

    @Test
    fun test_successfully_create_new_counter_when_not_exist() {
        val counterRepository = mock<CounterRepository>()
        val counterService = CounterService(counterRepository)
        val createCounterRequest = CreateCounterRequest("test_counter", 0)
        `when`(counterRepository.checkExistsAndSave(any(), any())).thenReturn(CounterData("test_counter", 0))

        counterService.createCounter(createCounterRequest)

        verify(counterRepository).checkExistsAndSave("test_counter", 0)
    }

    @Test
    fun test_successfully_increment_existing_counter_by_name() {
        val counterRepository = mock<CounterRepository>()
        val counterService = CounterService(counterRepository)
        `when`(counterRepository.checkExistsAndIncrement(any())).thenReturn(1)

        val result = counterService.incrementCounter("test_counter")

        assertEquals(1, result)
        verify(counterRepository).checkExistsAndIncrement("test_counter")
    }

    @Test
    fun test_successfully_delete_existing_counter() {
        val counterRepository = mock<CounterRepository>()
        val counterService = CounterService(counterRepository)
        val counterName = "test_counter"

        counterService.deleteCounter(counterName)

        verify(counterRepository).deleteIfExist(counterName)
    }

    @Test
    fun test_successfully_retrieve_all_counters_with_limit() {
        val counterRepository = mock<CounterRepository>()
        `when`(counterRepository.findAll(any())).thenReturn(listOf(CounterData("counter1", 10), CounterData("counter2", 20)))
        val counterService = CounterService(counterRepository)
        val limit = 2

        val result = counterService.getAllCounters(limit)

        assertEquals(2, result.size)
        assertEquals("counter1", result[0].name)
        assertEquals(10, result[0].value)
        assertEquals("counter2", result[1].name)
        assertEquals(20, result[1].value)
    }
}