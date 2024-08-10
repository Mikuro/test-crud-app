package com.pushpyshev.model.dto.counter.read

import com.pushpyshev.model.dto.counter.CounterData
import kotlinx.serialization.Serializable

@Serializable
data class ReadAllCountersResponse (val counters: List<CounterData>)