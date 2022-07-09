package com.madcamp.project2.Data

data class ResponseType<T>(
    val success: Boolean,
    val message: String,
    val data: T?
)
