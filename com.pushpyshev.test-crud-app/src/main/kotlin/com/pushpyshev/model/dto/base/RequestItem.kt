package com.pushpyshev.model.dto.base

import kotlinx.serialization.Serializable

@Serializable
data class RequestItem<T> (
    val meta: Meta,
    val data: T
)