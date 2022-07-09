package com.madcamp.project2.Auth

import android.content.Intent
import android.os.Bundle
import android.telecom.Call
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.madcamp.project2.Data.ResponseType
import com.madcamp.project2.Data.User
import com.madcamp.project2.Global
import com.madcamp.project2.R
import com.madcamp.project2.Service.PreferenceManager
import com.madcamp.project2.Service.ServiceCreator
import retrofit2.Callback
import retrofit2.Response
import kotlin.properties.Delegates

class InfoActivity : AppCompatActivity() {
    private val RC_SIGN_IN: Int = 101
    private val TAG: String = InfoActivity::class.java.simpleName
    private var userId: Int? = null
    private var user: User? = null
    lateinit var googleLoginButton: SignInButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initViews()
        initListeners()
        initUser()
    }

    private fun initViews() {
        googleLoginButton = findViewById(R.id.googleSignInButton)
    }

    private fun initListeners() {
        // --------------------------------------구글 로그인--------------------------------------------------------
        googleLoginButton.setOnClickListener{
            Log.d(TAG, "google login button click")
            googleSignIn()
        }
    }

    private fun initUser() {
        userId = intent.getIntExtra("userId", -1)

        val call: retrofit2.Call<ResponseType<User>> =
            ServiceCreator.userService.getUserInfo(Global.headers, userId!!)

        call.enqueue(object : Callback<ResponseType<User>> {
            override fun onResponse(
                call: retrofit2.Call<ResponseType<User>>,
                response: Response<ResponseType<User>>
            ) {
                if (response.code() == 200) {
                    user = response.body()?.data
                }
                else {
                    Toast.makeText(this@InfoActivity, response.body()?.message, Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: retrofit2.Call<ResponseType<User>>, t: Throwable) {
                Log.e("NetworkTest", "error:$t")
            }
        })
    }

    private fun googleSignIn() {
        if(Global.currentUserId != null) return

        val signInIntent: Intent = Global.mGoogleSignInClient.signInIntent
        Log.d(TAG, "start google sign in")
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            val idToken: String? = account.idToken
            PreferenceManager.setString(this@InfoActivity, "JWT", idToken?:"")
            Log.d(TAG, "google login Success")
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.statusCode)
        }
    }
}