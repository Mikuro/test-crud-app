package com.pushpyshev.routes.counter

import com.pushpyshev.exception.*
import com.pushpyshev.model.dto.base.ErrorResponseItem
import com.pushpyshev.model.dto.base.ResponseItem
import com.pushpyshev.model.dto.counter.read.ReadCounterResponse
import com.pushpyshev.plugin.withMDCContext
import com.pushpyshev.service.CounterService
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.counterByName(counterService: CounterService) {
    get("/api/v1/counters") {
        val name = call.parameters["name"] ?: throw validationException("LOYwXX")
        runCatching {
            withMDCContext("counterByName") { counterService.readCounter(name) }
        }.fold(
            onSuccess = { call.respond(ResponseItem(ReadCounterResponse(it))) },
            onFailure = {
                when (it) {
                    is AppException -> call.respond(ErrorResponseItem(ExceptionData(it.locationId, it.code, it.message)))
                    else -> call.respond(ResponseItem(ExceptionData("D7mRDH", UNEXPECTED_ERROR_CODE, UNEXPECTED_ERROR_MESSAGE)))
                }
            }
        )
    }
}