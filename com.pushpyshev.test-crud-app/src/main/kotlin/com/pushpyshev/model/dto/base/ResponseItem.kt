package com.pushpyshev.model.dto.base

import kotlinx.serialization.Serializable
import java.time.Instant

@Serializable
class ResponseItem<T> {
    private val status: String
    private val actualTimestamp: Long
    private val data: T?

    constructor(data: T) {
        this.data = data
        this.status = "success"
        this.actualTimestamp = Instant.now().toEpochMilli()
    }
}