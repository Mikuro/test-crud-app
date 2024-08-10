package com.pushpyshev.model.dto.counter.read

import kotlinx.serialization.Serializable

@Serializable
data class ReadCounterResponse (
    val value: Int
)