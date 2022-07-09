package com.madcamp.project2

import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.madcamp.project2.Data.User

object Global {
    lateinit var GSO: GoogleSignInOptions
    lateinit var mGoogleSignInClient: GoogleSignInClient
    var headers = HashMap<String, String>()
    val BASE_URL = "http://192.249.18.176/api/"
    var currentUserId: Int? = null
}