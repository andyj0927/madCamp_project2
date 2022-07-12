package com.madcamp.project2.Data

data class Game(
    val id: Int,
    val c_id: Int,
    val d_id: Int,
    val num_arr: String,
    val c_score: Int,
    val d_score: Int
)

data class RequestGame (
    val c_id: Int,
    val d_id: Int,
    val num_arr: String
    )
