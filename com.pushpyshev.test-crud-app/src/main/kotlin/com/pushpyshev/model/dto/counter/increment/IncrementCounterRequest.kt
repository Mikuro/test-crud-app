package com.pushpyshev.model.dto.counter.increment

import kotlinx.serialization.Serializable

@Serializable
data class IncrementCounterRequest(val name : String)
