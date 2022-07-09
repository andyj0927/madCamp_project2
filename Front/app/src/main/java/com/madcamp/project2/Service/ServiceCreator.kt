package com.madcamp.project2.Service

import okhttp3.*
import java.io.IOException;
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ServiceCreator {
    private var BASE_URL = "http://172.10.18.176/api/"

    private val client: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(BasicInterceptor("sagjin0000@naver.com", "1234")).build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    val userService: UserService = retrofit.create(UserService::class.java)
}