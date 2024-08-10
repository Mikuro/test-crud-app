package com.pushpyshev.model.dto.base;

import kotlinx.serialization.Serializable
import java.time.Instant

@Serializable
class ErrorResponseItem<T> {
    private val status: String
    private val actualTimestamp: Long
    private val data: T

    constructor(data: T) {
        this.data = data
        this.status = "error"
        this.actualTimestamp = Instant.now().toEpochMilli()
    }
}

