package com.madcamp.project2.Service

import com.madcamp.project2.Data.*
import retrofit2.Call
import retrofit2.http.*

interface UserService {
    @Headers("Content-Type: application/json")
    @GET("auth/user-list/get")
    fun getUserList(): Call<UserResponse<ArrayList<User>>>

    @Headers("Content-Type: application/json")
    @POST("auth/register")
    fun postRegister(
        @Body body: UserRegisterRequest
    ): Call<UserResponse<User>>

    @Headers("Content-Type: application/json")
    @POST("auth/login")
    fun postLogin(
        @Body body: UserLoginRequest
    ): Call<UserResponse<User>>

    @Headers("Content-Type: text/html")
    @GET("auth/logout")
    fun getLogout(): Call<UserResponse<Unit>>
}