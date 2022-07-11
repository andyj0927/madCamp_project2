package com.madcamp.project2

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.madcamp.project2.Data.User
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

    fun initReceiveChallengeSocket(activity: Activity?){
        if(currentUserId != null) {
            socket?.on("Challenge receive") { args ->
                val c_id: Int = args[0] as Int
                
                val alertDialogBuilder: AlertDialog.Builder? = activity?.let {
                    AlertDialog.Builder(it)
                }

                alertDialogBuilder?.setMessage("게임을 수락하시겠습니까?")
                    ?.setTitle("게임 신청을 받았습니다.")

                alertDialogBuilder?.apply {
                    setPositiveButton("네"){ dialog, id ->
                        run {
                            val data = JSONObject()
                            try {
                                data.put("c_id", c_id)
                                data.put("d_id", currentUserId)

                                socket?.emit("dual true", data)
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }

                        setNegativeButton("아니요") { dialog, id ->
                            run {
                                try {
                                    socket?.emit("dual false")
                                } catch (e: Exception) {
                                    e.printStackTrace()

                                }
                            }
                        }
                    }
                }

                alertDialogBuilder?.create()
            }
        }
    }

    const val CONNECT_USER = "connect user"
    const val GET_ALL_USER = "get all user"
    const val UPDATE_DATA = "dataUpdate"
    const val CONNECTED = "Connected"
    const val ON_TYPING = "on typing"
    const val SING_UP = "SingUp"
    const val SING_IN = "SingIn"
    const val USER_FRAGMENT = "User"
    const val GROUP_FRAGMENT = "Group"
    const val TYPE_CHAT = "typeChat"
    const val USER_ID = "userId"
    const val IS_CONNECTING = "isConnecting"
    const val IS_LOGIN = "isLogin"
    const val USER_NAME = "USER_NAME"
    const val ALL_GROUP = "All Group"
    const val ALL_GROUP2 = "AllGroup"

    const val CHAT = "Chat"
    const val REGISTRATION = "Register"
    const val TYPE_INTENT_IMAGE = "image/*"

    const val REQUEST_IMAGE_CODE = 1

    const val NAME_FILE_PREF = "Demo Chat"
    const val DATA_USER_NAME = "dataUSerName"
    const val ID_DEVi = "ipDevi"
    const val USER_RECIPIENT = "userRecipient"
}