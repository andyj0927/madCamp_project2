package com.madcamp.project2

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.madcamp.project2.Auth.InfoActivity
import com.madcamp.project2.Game.GameActivity
import com.madcamp.project2.Home.MainActivity
import io.socket.client.IO
import io.socket.client.Socket
import org.json.JSONObject

object Global {
    lateinit var GSO: GoogleSignInOptions
    lateinit var mGoogleSignInClient: GoogleSignInClient
    var headers = HashMap<String, String>()
    var currentUserId: Int? = null

    // ---------------------- SOCKET ----------------------------
    const val WS_BASE_URL = "http://192.249.18.176/"
    var socket: Socket? = null

}