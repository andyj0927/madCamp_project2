package com.madcamp.project2.Service

import com.madcamp.project2.Global
import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class BasicInterceptor constructor(private val username: String, private val password: String): Interceptor {
    private val credentials: String = Credentials.basic(username, password)

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {

        val newReq = chain.request().newBuilder()
            .addHeader("Authorization", credentials)
            .addHeader("Content-Type", "application/json")
            .addHeader("token", Global.token?: "")
            .build()

        return chain.proceed(newReq)
    }
}