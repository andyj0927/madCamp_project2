package com.madcamp.project2

import android.content.Context
import android.content.Intent
import android.util.Log
import com.madcamp.project2.Auth.LoginActivity
import com.madcamp.project2.Data.User
import com.madcamp.project2.Data.UserResponse
import com.madcamp.project2.Service.ServiceCreator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object Global {
    var currentUser: User? = null

    fun getCurrentUser(context: Context){
        val call: Call<UserResponse> =
            ServiceCreator.userService.getCurrentUser("application/json")

        call.enqueue(object : Callback<UserResponse> {
            override fun onResponse(
                call: Call<UserResponse>,
                response: Response<UserResponse>
            ) {
                if (response.code() == 200) {
                    currentUser = response.body()?.data
                }
                else if(response.code() == 101) {
                    currentUser = null
                    val intent = Intent(context, LoginActivity::class.java)
                    context.startActivity(intent)
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                Log.e("NetworkTest", "error:$t")
            }
        })
    }
}