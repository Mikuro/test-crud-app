package com.pushpyshev.model.dto.counter.delete

import kotlinx.serialization.Serializable

@Serializable
data class DeleteCounterRequest(val name: String)