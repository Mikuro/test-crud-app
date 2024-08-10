package com.pushpyshev.routes.counter

import com.pushpyshev.exception.AppException
import com.pushpyshev.exception.ExceptionData
import com.pushpyshev.exception.UNEXPECTED_ERROR_CODE
import com.pushpyshev.exception.UNEXPECTED_ERROR_MESSAGE
import com.pushpyshev.model.dto.base.ErrorResponseItem
import com.pushpyshev.model.dto.base.RequestItem
import com.pushpyshev.model.dto.base.ResponseItem
import com.pushpyshev.model.dto.counter.increment.IncrementCounterRequest
import com.pushpyshev.model.dto.counter.increment.IncrementCounterResponse
import com.pushpyshev.plugin.withMDCContext
import com.pushpyshev.service.CounterService
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.incrementCounter(counterService: CounterService) {
    patch("/api/v1/counters/increment") {
        val data = call.receive<RequestItem<IncrementCounterRequest>>().data
        runCatching {
            withMDCContext("incrementCounter") { counterService.incrementCounter(data.name) }
        }.fold(
            onSuccess = { call.respond(ResponseItem(IncrementCounterResponse(it))) },
            onFailure = {
                when (it) {
                    is AppException -> call.respond(ErrorResponseItem(ExceptionData(it.locationId, it.code, it.message)))
                    else -> call.respond(ResponseItem(ExceptionData("cnx4yp", UNEXPECTED_ERROR_CODE, UNEXPECTED_ERROR_MESSAGE)))
                }
            }
        )
    }
}