package com.madcamp.project2.Auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.madcamp.project2.Data.GoogleRequest
import com.madcamp.project2.Data.ResponseType
import com.madcamp.project2.Data.User
import com.madcamp.project2.Game.GameActivity
import com.madcamp.project2.Global
import com.madcamp.project2.Service.ServiceCreator
import com.madcamp.project2.databinding.ActivityInfoBinding
import io.socket.client.IO
import io.socket.client.Socket
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

class InfoActivity : AppCompatActivity() {
    private val RC_SIGN_IN: Int = 101
    private val TAG: String = InfoActivity::class.java.simpleName
    private var userId: Int? = null
    private var user: User? = null
    private var mBinding: ActivityInfoBinding? = null
    private val binding get() = mBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBinding()
        initListeners()
        initUser()
        initUIs()
    }

    private fun initBinding() {
        mBinding = ActivityInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun initListeners() {
        // --------------------------------------구글 로그인--------------------------------------------------------
        binding.googleSignInButton.setOnClickListener{
            Log.d(TAG, "google login button click")
            val account: GoogleSignInAccount? = GoogleSignIn.getLastSignedInAccount(this@InfoActivity)
            if(account == null) googleSignIn()

            else {
                binding.googleSignInButton.visibility = View.GONE
                binding.completeGoogleSignInTextView.visibility = View.VISIBLE
            }
        }


        // ------------------------------------ 대겳 버튼 -------------------------------------------
        binding.toBattle.setOnClickListener{
            Log.d(TAG, "Let's go to battle")

            val data = JSONObject()
            try {
                data.put("c_id", Global.currentUserId)
                data.put("d_id", userId)

                Log.d(TAG, "battle: $data")

                Global.socket?.emit("Dual Submit", data)

                val intent = Intent(this@InfoActivity, GameActivity::class.java)
                intent.putExtra("c_id", Global.currentUserId)
                    .putExtra("d_id", userId)

                startActivity(intent)

            } catch(e: JSONException) {
                e.printStackTrace()
            }
        }
    }

    private fun initUIs() {
        Log.d(TAG, "${Global.currentUserId} : ${userId}")
        if(Global.currentUserId == userId || user?.currentlyActive == 0) {
            binding.toBattle.visibility = View.GONE
        } else {
            binding.toBattle.visibility = View.VISIBLE
        }
    }

    private fun initUser() {
        userId = intent.getIntExtra("id", -1)
        Log.d(TAG, "$userId")

        getUser(userId!!)
    }

    private fun getUser(id: Int) {
        val call: retrofit2.Call<ResponseType<User>> =
            ServiceCreator.userService.getUserInfo(Global.headers, id)

        val thread = Thread {
            try {
                user = call.execute().body()?.data
            } catch(e: IOException) {
                e.printStackTrace()
            }
        }
        thread.start()

        try {
            thread.join()
            binding.name.text = user?.displayName!!
            binding.totalGame.text = (user?.total!!).toString()
            binding.win.text = (user?.win!!).toString()
            binding.draw.text = (user?.draw!!).toString()
            binding.lose.text = (user?.lose!!).toString()

            if (user?.google != null) {
                binding.googleSignInButton.visibility = View.GONE
                binding.completeGoogleSignInTextView.visibility = View.VISIBLE
            }
        } catch(e: Exception){
            e.printStackTrace()
        }
    }

    private fun googleSignIn() {
        val signInIntent: Intent = Global.mGoogleSignInClient.signInIntent
        Log.d(TAG, "start google sign in")
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d(TAG, "$requestCode : $resultCode")
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            Log.d(TAG, "task에서 문제가 생기나? $task")
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            Log.d(TAG, "account: $account")
            val idToken: String? = account.idToken


            val call: retrofit2.Call<ResponseType<Unit>> =
                ServiceCreator.userService.interlockBetweenLocalAndGoogle(Global.headers, GoogleRequest(idToken!!))

            val thread = Thread {
                try {
                    call.execute()
                } catch(e: IOException) {
                    e.printStackTrace()
                }
            }
            thread.start()

            try {
                thread.join()
                Toast.makeText(this@InfoActivity, "연동 성공", Toast.LENGTH_LONG)
                finish()
                startActivity(intent.putExtra("id", userId))
                Log.d(TAG, "google login Success")
            } catch(e: Exception){
                e.printStackTrace()
            }

        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.statusCode)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding = null
    }
}