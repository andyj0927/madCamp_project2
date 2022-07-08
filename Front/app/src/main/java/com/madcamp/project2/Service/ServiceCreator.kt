package com.madcamp.project2.Service

import okhttp3.*
import java.io.IOException;
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ServiceCreator {
    private var BASE_URL = "http://172.10.5.116/api/"

    private val commonNetWorkInterceptor = object: Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val newReq = chain.request().newBuilder()
                .addHeader("Authorization"m)
        }
    }

    private var client: OkHttpClient = OkHttpClient.Builder().addInterceptor(interceptor).build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val userService: UserService = retrofit.create(UserService::class.java)
}