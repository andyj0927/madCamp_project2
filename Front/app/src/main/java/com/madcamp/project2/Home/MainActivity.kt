package com.madcamp.project2.Home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.madcamp.project2.Auth.LoginActivity
import com.madcamp.project2.Auth.RegisterActivity
import com.madcamp.project2.Data.User
import com.madcamp.project2.Data.UserListResponse
import com.madcamp.project2.R
import com.madcamp.project2.Service.ServiceCreator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    lateinit var list: ArrayList<User>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getUserList()
    }

    private fun getUserList() {
        val call: Call<UserListResponse> =
            ServiceCreator.userService.getUserList("application/json")

        call.enqueue(object : Callback<UserListResponse> {
            override fun onResponse(
                call: Call<UserListResponse>,
                response: Response<UserListResponse>
            ) {
                if (response.code() == 200) {
                    list = response.body()?.data?: ArrayList()

                    startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                }
                else if(response.code() == 500) {
                    val data = response.body()?.data

                    Toast.makeText(this@MainActivity, "$data", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<UserListResponse>, t: Throwable) {
                Log.e("NetworkTest", "error:$t")
            }
        })
    }
}