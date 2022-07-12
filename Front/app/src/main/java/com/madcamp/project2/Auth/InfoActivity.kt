package com.madcamp.project2.Auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.madcamp.project2.Data.ResponseType
import com.madcamp.project2.Data.User
import com.madcamp.project2.Game.GameActivity
import com.madcamp.project2.Global
import com.madcamp.project2.Service.ServiceCreator
import com.madcamp.project2.databinding.ActivityInfoBinding
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

class InfoActivity : AppCompatActivity() {
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
        // ------------------------------------ 대결 버튼 -------------------------------------------
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
        Log.d(TAG, "${Global.currentUserId} : $userId")
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
        } catch(e: Exception){
            e.printStackTrace()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding = null
    }
}