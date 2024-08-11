package com.pushpyshev.exception

data class AppException(val locationId: String, val code: String, override val message: String) : RuntimeException(message) {
}