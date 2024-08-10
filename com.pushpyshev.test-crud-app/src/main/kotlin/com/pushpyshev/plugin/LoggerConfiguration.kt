package com.pushpyshev.plugin

import io.ktor.server.application.*
import io.ktor.server.plugins.callloging.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.slf4j.MDCContext
import kotlinx.coroutines.withContext
import org.slf4j.MDC
import java.util.*

fun Application.configureLogging() {
    install(CallLogging) {}
}

suspend inline fun <R> withMDCContext(
    businessThread: String, crossinline block: suspend CoroutineScope.() -> R
): R {
    val requestId = UUID.randomUUID().toString()

    return withContext(MDCContext(mapOf("requestId" to requestId, "businessThread" to businessThread))) {
        try {
            block(this)
        } finally {
            MDC.clear()
        }
    }
}