package com.madcamp.project2.Auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.madcamp.project2.Data.User
import com.madcamp.project2.Data.UserRegisterRequest
import com.madcamp.project2.Data.ResponseType
import com.madcamp.project2.R
import com.madcamp.project2.Service.ServiceCreator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {
    lateinit var editId: EditText
    lateinit var editDisplayName: EditText
    lateinit var editPassword: EditText
    lateinit var button: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        initViews()
        initListeners()
    }

    private fun initViews() {
        editId = findViewById(R.id.registerId)
        editDisplayName = findViewById(R.id.registerDisplayName)
        editPassword = findViewById(R.id.registerPassword)
        button = findViewById(R.id.registerButton)
    }

    private fun initListeners() {
        button.setOnClickListener {
            val userName = editId.text.toString()
            val displayName = editDisplayName.text.toString()
            val password = editPassword.text.toString()

            val newUser = UserRegisterRequest(userName, displayName, password)

            val call: Call<ResponseType<User>> =
                ServiceCreator.userService.postRegister(newUser)

            call.enqueue(object : Callback<ResponseType<User>> {
                override fun onResponse(
                    call: Call<ResponseType<User>>,
                    response: Response<ResponseType<User>>
                ) {
                    if (response.code() == 200) {
                        val data = response.body()?.data

                        Toast.makeText(
                            this@RegisterActivity,
                            "${data?.userName}님 반갑습니다!",
                            Toast.LENGTH_LONG
                        ).show()
                        startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                    }
                    else if (response.code() == 400) {
                        Toast.makeText(
                            this@RegisterActivity,
                            "id 또는 닉네임이 존재합니다.",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    else {
                        Toast.makeText(this@RegisterActivity, "회원가입 실패", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<ResponseType<User>>, t: Throwable) {
                    Log.e("NetworkTest", "error:$t")
                }
            })
        }
    }

}