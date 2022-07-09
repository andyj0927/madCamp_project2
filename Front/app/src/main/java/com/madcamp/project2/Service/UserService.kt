package com.madcamp.project2.Service

import com.madcamp.project2.Data.*
import retrofit2.Call
import retrofit2.http.*

interface UserService {
    @GET("auth/user-list/get")
    fun getUserList(): Call<UserResponse<ArrayList<User>>>

    @POST("auth/register")
    fun postRegister(
        @Body body: UserRegisterRequest
    ): Call<UserResponse<User>>

    @POST("auth/login")
    fun postLogin(
        @Body body: UserLoginRequest
    ): Call<UserResponse<User>>

    @GET("auth/logout")
    fun getLogout(): Call<UserResponse<Unit>>
}