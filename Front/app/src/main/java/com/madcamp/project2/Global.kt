package com.madcamp.project2

import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.madcamp.project2.Data.User

object Global {
    lateinit var GSO: GoogleSignInOptions
    lateinit var mGoogleSignInClient: GoogleSignInClient
    var pos1: Int = -1
    var pos2: Int = -1
    var headers = HashMap<String, String>()

    var currentUserId: Int? = null
}