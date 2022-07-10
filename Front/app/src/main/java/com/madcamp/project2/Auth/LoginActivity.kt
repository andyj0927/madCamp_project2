package com.madcamp.project2.Auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.madcamp.project2.Data.*
import com.madcamp.project2.Global
import com.madcamp.project2.Home.MainActivity
import com.madcamp.project2.Service.PreferenceManager
import com.madcamp.project2.Service.ServiceCreator
import com.madcamp.project2.databinding.ActivityLoginBinding
import retrofit2.Call
import java.io.IOException


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

            localLogin(userName, password)
        }
        // -------------------------------------------------------------------------------------------------------
        binding.googleSignInButton.setOnClickListener{
            val account: GoogleSignInAccount? = GoogleSignIn.getLastSignedInAccount(this@LoginActivity)
            if(account == null || account.idToken == null) {
                val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
                startActivity(intent)
            }

            else {
                googleLogin(account.idToken!!)
            }
        }

        binding.registerButton.setOnClickListener{
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun localLogin(userName: String, password: String) {
        val call: Call<ResponseType<Int>> =
            ServiceCreator.userService.postLogin(UserLoginRequest(userName, password))

        Log.d(TAG, "local Login is Executed")
        login(call)
    }

    private fun googleLogin(idToken: String) {
        val call: Call<ResponseType<Int>> =
            ServiceCreator.userService.getGoogleLogin(Global.headers, GoogleRequest(idToken))

        Log.d(TAG, "google Login is Executed")
        login(call)
    }

    private fun login(call: Call<ResponseType<Int>>) {
        var loginFlag = false

        val thread = Thread {
            try {
                Global.currentUserId = call.execute().body()?.data
                Log.d(TAG, "currentId: ${Global.currentUserId}")
                loginFlag = true

            } catch(e: IOException) {
                loginFlag = false
            }
        }
        thread.start()

        try {
            thread.join()
            Log.d(TAG, "$loginFlag")
            if(loginFlag) {
                setJwt()
            }
        } catch(e: Exception){
            e.printStackTrace()
        }
    }

    private fun setJwt() {
        Log.d(TAG, "before setJwt, currentId: ${Global.currentUserId}")
        val call: Call<ResponseType<String>> =
            ServiceCreator.jwtService.setJwt(Global.headers, JwtRequest(Global.currentUserId!!))

        /*
        jwtCall.enqueue(object : Callback<ResponseType<String>> {
            override fun onResponse(
                call: Call<ResponseType<String>>,
                response: Response<ResponseType<String>>
            ) {
                if (response.code() == 200) {
                    val token = response.body()?.data!!
                    PreferenceManager.setString(this@LoginActivity, "JWT", token)
                    Global.headers["token"] = token
                    Log.d(TAG, "token: $token")
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(this@LoginActivity, response.body()?.message?: "null", Toast.LENGTH_LONG)
                        .show()
                }
            }

            override fun onFailure(call: Call<ResponseType<String>>, t: Throwable) {
                Log.e("NetworkTest", "error: $t")
            }
        })

         */

        var flag = false
        val thread = Thread {
            try {
                val token: String? = call.execute().body()?.data
                Log.d(TAG, "token: ${token}")
                PreferenceManager.setString(this@LoginActivity, "JWT", token!!)
                Global.headers["token"] = token
                flag = true
            } catch(e: IOException) {
                e.printStackTrace()
            }
        }
        thread.start()

        try {
            thread.join()
            Log.d(TAG, "$flag")
            if(flag) {
                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                startActivity(intent)
            }
        } catch(e: Exception){
            e.printStackTrace()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding = null
    }
}