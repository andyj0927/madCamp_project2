package com.madcamp.project2

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.madcamp.project2.Home.MainActivity
import com.madcamp.project2.Service.PreferenceManager

class SplashActivity : AppCompatActivity() {
    private val TAG = this::class.java.simpleName
    private val duration: Long = 1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        initGSO()
        initHeaders()
        init()
    }

    private fun init() {
        Handler(Looper.getMainLooper()).postDelayed({
            intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, duration)
    }

    private fun initHeaders() {
        Global.headers["Content-Type"] = "application/json"
        Global.headers["token"] = PreferenceManager.getString(this@SplashActivity, PreferenceManager.PREFERENCES_NAME)

        Log.d(TAG, "${Global.headers["token"]}")
    }

    private fun initGSO() {
        Global.GSO = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.server_client_id))
            .build()

        Global.mGoogleSignInClient = GoogleSignIn.getClient(this@SplashActivity, Global.GSO)
    }
}