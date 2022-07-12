package com.madcamp.project2.Service

import com.madcamp.project2.Data.*
import retrofit2.Call
import retrofit2.http.*

interface UserService {
    @GET("auth/user-list")
    fun getUserList(
        @HeaderMap headers: Map<String, String>
    ): Call<ResponseType<ArrayList<User>>>

    @POST("auth/register")
    fun postRegister(
        @Body body: UserRegisterRequest
    ): Call<ResponseType<User>>

    @POST("auth/login")
    fun postLogin(
        @Body body: UserLoginRequest
    ): Call<ResponseType<Int>>

    @GET("auth/logout")
    fun getLogout(
        @HeaderMap headers: Map<String, String>
    ): Call<ResponseType<Unit>>

    @GET("auth/Info/{id}")
    fun getUserInfo(
        @HeaderMap headers: Map<String, String>,
        @Path("id") id: Int
    ): Call<ResponseType<User>>

    @POST("auth/token")
    fun getUserByToken(
        @HeaderMap headers: Map<String, String>
    ): Call<ResponseType<Int>>
}