package com.madcamp.project2.Auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.madcamp.project2.Data.User
import com.madcamp.project2.Data.UserLoginRequest
import com.madcamp.project2.Data.UserResponse
import com.madcamp.project2.Global
import com.madcamp.project2.Home.MainActivity
import com.madcamp.project2.R
import com.madcamp.project2.Service.ServiceCreator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    lateinit var editTextId: EditText
    lateinit var editTextPassword: EditText
    lateinit var buttonLogin: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initViews()
        initListeners()
    }

    private fun initViews() {
        editTextId = findViewById(R.id.loginId)
        editTextPassword = findViewById(R.id.loginPW)
        buttonLogin = findViewById(R.id.loginButton)
    }


    private fun initListeners() {
        buttonLogin.setOnClickListener {
            val userName = editTextId.text.toString()
            val password = editTextPassword.text.toString()

            if(userName == "" || password == "") {
                Toast.makeText(this@LoginActivity, "빈 칸이 있습니다.", Toast.LENGTH_LONG).show()
            }

            else {
                val user = UserLoginRequest(userName, password)

                val call: Call<UserResponse<User>> =
                    ServiceCreator.userService.postLogin(user)

                call.enqueue(object : Callback<UserResponse<User>> {
                    override fun onResponse(
                        call: Call<UserResponse<User>>,
                        response: Response<UserResponse<User>>
                    ) {
                        if (response.code() == 200) {
                            val data = response.body()?.data
                            Global.currentUser = data
                            Toast.makeText(
                                this@LoginActivity,
                                "${data?.userName}님 반갑습니다!",
                                Toast.LENGTH_LONG
                            ).show()
                            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                            finish()
                        }
                        else if (response.code() == 400) {
                            Toast.makeText(this@LoginActivity, "ID 또는 비밀번호가 틀렸습니다.", Toast.LENGTH_LONG).show()
                        }
                        else if (response.code() == 500) {
                            Toast.makeText(this@LoginActivity, "Internal Server error", Toast.LENGTH_LONG).show()
                        }
                    }

                    override fun onFailure(call: Call<UserResponse<User>>, t: Throwable) {
                        Log.e("NetworkTest", "error:$t")
                    }
                })
            }



        }
    }
}