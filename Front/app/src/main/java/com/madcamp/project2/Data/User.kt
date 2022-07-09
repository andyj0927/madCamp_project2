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
    val google: String?,
    val createdAt: Date,
    val updatedAt: Date
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