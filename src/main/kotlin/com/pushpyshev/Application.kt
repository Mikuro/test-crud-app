package com.pushpyshev

import com.pushpyshev.plugin.configureDatabases
import com.pushpyshev.plugin.configureLogging
import com.pushpyshev.plugin.configureSerialization
import com.pushpyshev.plugins.counterRoutes
import io.ktor.server.application.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)


fun Application.module() {
    configureSerialization()
    configureDatabases()
    configureLogging()
    counterRoutes()
}
