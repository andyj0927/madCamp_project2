package com.madcamp.project2.Service

import com.madcamp.project2.Data.Game
import com.madcamp.project2.Data.RequestGame
import com.madcamp.project2.Data.ResponseType
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.HeaderMap
import retrofit2.http.POST

interface GameService {
    @POST("game/data-list")
    fun postGame(
        @HeaderMap headers: Map<String, String>,
        @Body num_arr: RequestGame
    ): Call<ResponseType<Game>>

}