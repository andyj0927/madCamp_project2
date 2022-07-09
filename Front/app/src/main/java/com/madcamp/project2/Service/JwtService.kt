package com.madcamp.project2.Service

import com.madcamp.project2.Data.ResponseType
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.HeaderMap

interface JwtService {
    @GET("/jwt")
    fun getJwt(
        @HeaderMap headers: HashMap<String, String>
    ): Call<ResponseType<String>>
}