package com.pushpyshev.exception

class AppException(val locationId: String, val code: String, message: String) : RuntimeException(message) {
}