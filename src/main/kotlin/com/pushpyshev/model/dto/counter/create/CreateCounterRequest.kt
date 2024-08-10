package com.pushpyshev.model.dto.counter.create

import kotlinx.serialization.Serializable

@Serializable
data class CreateCounterRequest(val name: String, val value: Int)