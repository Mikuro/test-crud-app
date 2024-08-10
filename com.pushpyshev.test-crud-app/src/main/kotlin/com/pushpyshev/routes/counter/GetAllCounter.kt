package com.pushpyshev.routes.counter

import com.pushpyshev.exception.AppException
import com.pushpyshev.exception.ExceptionData
import com.pushpyshev.exception.UNEXPECTED_ERROR_CODE
import com.pushpyshev.exception.UNEXPECTED_ERROR_MESSAGE
import com.pushpyshev.model.dto.base.ErrorResponseItem
import com.pushpyshev.model.dto.base.ResponseItem
import com.pushpyshev.model.dto.counter.read.ReadAllCountersResponse
import com.pushpyshev.plugin.withMDCContext
import com.pushpyshev.service.CounterService
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.getAllCounters(env: ApplicationEnvironment?, counterService: CounterService) {
    get("/api/v1/counters/all") {
        val limitCounters = env?.config?.config("ktor.routing")?.property("counterLimit")?.getString()?.toInt() ?: 0
        runCatching {
            withMDCContext("getAllCounters") { counterService.getAllCounters(limitCounters) }
        }.fold(
            onSuccess = { call.respond(ResponseItem(ReadAllCountersResponse(it))) },
            onFailure = {
                when (it) {
                    is AppException -> call.respond(ErrorResponseItem(ExceptionData(it.locationId, it.code, it.message)))
                    else -> call.respond(ResponseItem(ExceptionData("a4o7j2", UNEXPECTED_ERROR_CODE, UNEXPECTED_ERROR_MESSAGE)))
                }
            }
        )
    }
}