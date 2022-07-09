package com.madcamp.project2.Auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.madcamp.project2.Data.ResponseType
import com.madcamp.project2.Data.User
import com.madcamp.project2.Data.UserLoginRequest
import com.madcamp.project2.Global
import com.madcamp.project2.Home.MainActivity
import com.madcamp.project2.Service.PreferenceManager
import com.madcamp.project2.Service.ServiceCreator
import com.madcamp.project2.databinding.ActivityLoginBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginActivity : AppCompatActivity() {
    private val TAG: String = this.javaClass.simpleName
    private var mBinding: ActivityLoginBinding? = null
    private val binding get() = mBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding()
        initListeners()
    }

    private fun initBinding() {
        mBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun initListeners() {
        // --------------------------------- 로그인 버튼 -------------------------------------------
        binding.loginButton.setOnClickListener {
            val userName = binding.loginId.text.toString()
            val password = binding.loginPW.text.toString()

            if(userName == "" || password == "") {
                Toast.makeText(this@LoginActivity, "빈 칸이 있습니다.", Toast.LENGTH_LONG).show()
            }

            val user = UserLoginRequest(userName, password)

            val call: Call<ResponseType<User>> =
                ServiceCreator.userService.postLogin(user)

            call.enqueue(object : Callback<ResponseType<User>> {
                override fun onResponse(
                    call: Call<ResponseType<User>>,
                    response: Response<ResponseType<User>>
                ) {
                    if (response.code() == 200) {
                        Global.currentUserId = response.body()?.data?.id

                        val jwtCall: Call<ResponseType<String>> =
                            ServiceCreator.jwtService.getJwt(Global.headers)

                        jwtCall.enqueue(object: Callback<ResponseType<String>> {
                            override fun onResponse(
                                call: Call<ResponseType<String>>,
                                response: Response<ResponseType<String>>
                            ) {
                                if(response.code() == 200) {
                                    val token : String = response.body()?.data!!
                                    PreferenceManager.setString(this@LoginActivity, "JWT", token)
                                    Global.headers["token"] = token
                                }

                                else {
                                    Toast.makeText(this@LoginActivity, response.body()?.message, Toast.LENGTH_LONG)
                                        .show()
                                }
                            }

                            override fun onFailure(call: Call<ResponseType<String>>, t: Throwable) {
                                Log.e("NetworkTest", "error: $t")
                            }
                        })

                        ServiceCreator.jwtService.getJwt(Global.headers)
                        Toast.makeText(
                            this@LoginActivity,
                            "로그인 되었습니다.",
                            Toast.LENGTH_LONG
                        ).show()

                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                        finish()
                    } else if (response.code() == 400) {
                        Toast.makeText(this@LoginActivity, "ID 또는 비밀번호가 틀렸습니다.", Toast.LENGTH_LONG).show()
                    } else if (response.code() == 403) {
                        Toast.makeText(this@LoginActivity, "ID가 없습니다.", Toast.LENGTH_SHORT).show()
                    } else if (response.code() == 500) {
                        Toast.makeText(this@LoginActivity, "Internal Server error", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<ResponseType<User>>, t: Throwable) {
                    Log.e("NetworkTest", "error:$t")
                }
            })
        }
        // -------------------------------------------------------------------------------------------------------
        binding.googleSignInButton.setOnClickListener{
            val account: GoogleSignInAccount? = GoogleSignIn.getLastSignedInAccount(this@LoginActivity)
            if(account == null || account.idToken == null) {
                val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
                startActivity(intent)
            }

            else {
                val call: Call<ResponseType<User>> =
                    ServiceCreator.userService.getGoogleLogin(Global.headers, account.idToken!!)

                call.enqueue(object : Callback<ResponseType<User>> {
                    override fun onResponse(
                        call: Call<ResponseType<User>>,
                        response: Response<ResponseType<User>>
                    ) {
                        if (response.code() == 200) {
                            Global.currentUserId = response.body()?.data?.id
                            val jwtCall: Call<ResponseType<String>> =
                                ServiceCreator.jwtService.getJwt(Global.headers)

                            jwtCall.enqueue(object: Callback<ResponseType<String>> {
                                override fun onResponse(
                                    call: Call<ResponseType<String>>,
                                    response: Response<ResponseType<String>>
                                ) {
                                    if(response.code() == 200) {
                                        val token : String = response.body()?.data!!
                                        PreferenceManager.setString(this@LoginActivity, "JWT", token)
                                        Global.headers["token"] = token
                                    }

                                    else {
                                        Toast.makeText(this@LoginActivity, response.body()?.message, Toast.LENGTH_LONG)
                                            .show()
                                    }
                                }

                                override fun onFailure(call: Call<ResponseType<String>>, t: Throwable) {
                                    Log.e("NetworkTest", "error: $t")
                                }
                            })

                            Toast.makeText(this@LoginActivity, "로그인 되었습니다.", Toast.LENGTH_LONG)
                                .show()

                            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                            finish()
                        } else if (response.code() == 400) {
                            Toast.makeText(this@LoginActivity, "ID 또는 비밀번호가 틀렸습니다.", Toast.LENGTH_LONG).show()
                        } else if (response.code() == 403) {
                            Toast.makeText(this@LoginActivity, "ID가 없습니다.", Toast.LENGTH_SHORT).show()
                        } else if (response.code() == 500) {
                            Toast.makeText(this@LoginActivity, "Internal Server error", Toast.LENGTH_LONG).show()
                        }
                    }

                    override fun onFailure(call: Call<ResponseType<User>>, t: Throwable) {
                        Log.e("NetworkTest", "error:$t")
                    }
                })
            }
        }
    }

    override fun onDestroy() {
        mBinding = null
        super.onDestroy()
    }
}