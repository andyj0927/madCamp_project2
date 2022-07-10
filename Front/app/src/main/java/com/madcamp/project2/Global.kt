package com.madcamp.project2

import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.madcamp.project2.Data.User
import io.socket.client.Socket

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

    const val CONNECT_USER = "connect user"
    const val GET_ALL_USER = "get all user"
    const val UPDATE_DATA = "dataUpdate"
    const val CHAT_MESSAGE = "chat message"
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