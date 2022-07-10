package com.madcamp.project2.Service

import com.madcamp.project2.Data.Jwt
import com.madcamp.project2.Data.JwtRequest
import com.madcamp.project2.Data.ResponseType
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.HeaderMap
import retrofit2.http.POST

interface JwtService {
    @POST("jwt/sign")
    fun setJwt(
        @HeaderMap headers: HashMap<String, String>,
        @Body id: JwtRequest
    ): Call<ResponseType<String>>
}