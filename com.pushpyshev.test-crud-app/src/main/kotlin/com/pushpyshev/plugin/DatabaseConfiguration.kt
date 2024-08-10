package com.pushpyshev.plugin

import io.ktor.server.application.*

fun Application.configureDatabases() {
    DatabaseFactory.init(environment)
    environment.monitor.subscribe(ApplicationStopped) {
        DatabaseFactory.close()
    }
}
