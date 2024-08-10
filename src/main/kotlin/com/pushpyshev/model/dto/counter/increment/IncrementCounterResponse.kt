package com.pushpyshev.model.dto.counter.increment

import kotlinx.serialization.Serializable

@Serializable
data class IncrementCounterResponse(val value : Int)