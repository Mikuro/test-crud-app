package com.pushpyshev.model.dto.counter

import kotlinx.serialization.Serializable

@Serializable
data class CounterData(val name : String, val value : Int)
