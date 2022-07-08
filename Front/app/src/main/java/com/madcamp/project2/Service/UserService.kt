package com.madcamp.project2.Service

import com.madcamp.project2.Data.*
import retrofit2.Call
import retrofit2.http.*

interface UserService {
    @GET("/auth/user-list/get")
    fun getUserList(
        @Header("Content-Type") content_type: String
    ): Call<UserListResponse>

    @GET("auth/user/get")
    fun getCurrentUser(
        @Header("Content-Type") content_type: String
    ): Call<UserResponse>

    @POST("auth/register")
    fun postRegister(
        @Header("Content-Type") content_type: String,
        @Body body: UserRegisterRequest
    ): Call<UserResponse>

    @POST("auth/login")
    fun postLogin(
        @Header("Content-Type") content_type: String,
        @Body body: UserLoginRequest
    ): Call<UserResponse>

    @GET("auth/logout")
    fun getLogout(
        @Header("Content-Type") content_type: String
    ): Call<UserResponse>
}