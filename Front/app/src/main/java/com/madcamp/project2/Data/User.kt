package com.madcamp.project2.Data

import java.util.*

data class User (
    val id: Int,
    val userName: String,
    val displayName: String,
    val win: Int,
    val draw: Int,
    val lose: Int,
    val total: Int,
    val friends: ArrayList<Int>,
    val currentlyActive: Int,
    val createdAt: Date,
    val updatedAt: Date
)

data class UserResponse<T>(
    val status: Int,
    val success: Boolean,
    val message: String,
    val data: T?
    )

data class UserRegisterRequest(
    val userName: String,
    val displayName: String,
    val password: String
    )

data class UserLoginRequest(
    val userName: String,
    val password: String
    )