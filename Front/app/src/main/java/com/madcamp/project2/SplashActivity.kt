package com.madcamp.project2

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.madcamp.project2.Data.ResponseType
import com.madcamp.project2.Game.GameActivity
import com.madcamp.project2.Home.MainActivity
import com.madcamp.project2.Service.PreferenceManager
import com.madcamp.project2.Service.ServiceCreator
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import org.json.JSONObject

import retrofit2.Call
import java.io.IOException

class SplashActivity : AppCompatActivity() {
    private val TAG = this::class.java.simpleName
    private val duration: Long = 1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)


        Global.socket = IO.socket(Global.WS_BASE_URL)
        initGSO()
        initHeaders()
        initCurrentUser()
        initMain()
    }

    private fun initCurrentUser() {
        val call: Call<ResponseType<Int>> =
            ServiceCreator.userService.getUserByToken(Global.headers)

        val thread = Thread {
            try {
                Global.currentUserId = call.execute().body()?.data
            } catch(e: IOException) {
                e.printStackTrace()
            }
        }
        thread.start()

        try {
            thread.join()
        } catch(e: Exception) {
            e.printStackTrace()
        }
    }

    private fun initMain() {
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

        if(Global.headers["token"] != "") {
            initCurrentUser()

            Global.socket?.connect()
            Global.socket?.emit("login", Global.currentUserId)
        }
    }

    private fun initGSO() {
        Global.GSO = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.server_client_id))
            .build()

        Global.mGoogleSignInClient = GoogleSignIn.getClient(this@SplashActivity, Global.GSO)
    }


}