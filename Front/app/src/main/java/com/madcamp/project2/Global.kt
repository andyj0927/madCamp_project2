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
    var pos1: Int = -1
    var pos2: Int = -1
    var headers = HashMap<String, String>()
    var currentUserId: Int? = null

    // ---------------------- SOCKET ----------------------------
    const val WS_BASE_URL = "http://192.249.18.176/"
    var socket: Socket? = null


    fun initSocket(activity: Activity){
        val simpleName = activity::class.java.simpleName
        if(currentUserId != null && simpleName != "GameActivity") {
            socket?.on("Dual receive") { args ->
                val c_id: Int = args[0] as Int
                Log.d("Game received", "from ${c_id}" )

                initOnGameReceive(activity, c_id)
            }
        }
    }

    private fun initOnGameReceive(activity: Activity, c_id: Int) {
        val alertDialogBuilder: AlertDialog.Builder = activity.let {
            AlertDialog.Builder(activity)
        }

        alertDialogBuilder.setMessage("게임을 수락하시겠습니까?")
            ?.setTitle("게임 신청을 받았습니다.")

        alertDialogBuilder.apply {
            setPositiveButton("네") { dialog, id ->
                val data = JSONObject()
                try {
                    data.put("c_id", c_id)
                    data.put("d_id", Global.currentUserId)

                    socket?.emit("dual accept", data)

                    val intent = Intent(activity, GameActivity::class.java)
                    activity.startActivity(intent)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            setNegativeButton("아니요") { dialog, id ->
                try {
                    socket?.emit("dual false")
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            Log.d(activity::class.java.simpleName, "alertDialog start")
            alertDialogBuilder.create()?.show()
        }, 0)
    }
}