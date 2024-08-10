package com.pushpyshev.routes.counter

import com.pushpyshev.exception.AppException
import com.pushpyshev.exception.ExceptionData
import com.pushpyshev.exception.UNEXPECTED_ERROR_CODE
import com.pushpyshev.exception.UNEXPECTED_ERROR_MESSAGE
import com.pushpyshev.model.dto.base.ErrorResponseItem
import com.pushpyshev.model.dto.base.RequestItem
import com.pushpyshev.model.dto.base.ResponseItem
import com.pushpyshev.model.dto.counter.create.CreateCounterRequest
import com.pushpyshev.plugin.withMDCContext
import com.pushpyshev.service.CounterService
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.createCounter(counterService: CounterService) {
    post("/api/v1/counters/create") {
        val data = call.receive<RequestItem<CreateCounterRequest>>().data
        runCatching {
            withMDCContext("createCounter") { counterService.createCounter(data) }
        }.fold(
            onSuccess = { call.respond(ResponseItem(it)) },
            onFailure = {
                when (it) {
                    is AppException -> call.respond(ErrorResponseItem(ExceptionData(it.locationId, it.code, it.message)))
                    else -> call.respond(ResponseItem(ExceptionData("Vl0gLQ", UNEXPECTED_ERROR_CODE, UNEXPECTED_ERROR_MESSAGE)))
                }
            }
        )
    }
}