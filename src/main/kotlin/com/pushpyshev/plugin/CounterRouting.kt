package com.pushpyshev.plugins

import com.pushpyshev.repository.CounterRepository
import com.pushpyshev.routes.counter.*
import com.pushpyshev.service.CounterService
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.counterRoutes() {
    val counterRepository = CounterRepository()
    val counterService = CounterService(counterRepository)
    routing {
        counterByName(counterService)
        createCounter(counterService)
        incrementCounter(counterService)
        deleteCounter(counterService)
        getAllCounters(environment, counterService)
    }
}