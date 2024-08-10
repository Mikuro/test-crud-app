package com.pushpyshev.exception

import kotlinx.serialization.Serializable

const val VALIDATION_ERROR_CODE: String = "ER001"
const val VALIDATION_ERROR_MESSAGE: String = "Request validation error"
const val INTERNAL_ERROR_CODE: String = "ER002"
const val INTERNAL_ERROR_MESSAGE: String = "Internal service error"
const val UNEXPECTED_ERROR_CODE: String = "ER003"
const val UNEXPECTED_ERROR_MESSAGE: String = "Unexpected service error"

fun validationException(locationId: String): AppException {
    return AppException(locationId, VALIDATION_ERROR_CODE, VALIDATION_ERROR_MESSAGE)
}

fun internalException(locationId: String): AppException {
    return AppException(locationId, INTERNAL_ERROR_CODE, INTERNAL_ERROR_MESSAGE)
}

@Serializable
data class ExceptionData(val locationId: String, val code: String, val message: String?)