package com.madcamp.project2.Home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import com.madcamp.project2.Data.User
import com.madcamp.project2.Data.UserResponse
import com.madcamp.project2.Global
import com.madcamp.project2.R
import com.madcamp.project2.Service.ServiceCreator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    lateinit var list: ArrayList<User>
    lateinit var mainRelativeLayout: RelativeLayout
    lateinit var testTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
        getUserList()
    }

    private fun initViews() {
        mainRelativeLayout = findViewById(R.id.mainRelativeLayout)
        testTextView = findViewById(R.id.testTextView)
    }

    private fun getUserList() {
        val call: Call<UserResponse<ArrayList<User>>> =
            ServiceCreator.userService.getUserList()

        call.enqueue(object : Callback<UserResponse<ArrayList<User>>> {
            override fun onResponse(
                call: Call<UserResponse<ArrayList<User>>>,
                response: Response<UserResponse<ArrayList<User>>>
            ) {
                if (response.code() == 200) {
                    list = response.body()?.data?: ArrayList()
                    Log.d("userList", list.toString())

                    mainRelativeLayout.visibility = View.VISIBLE
                    testTextView.visibility = View.GONE
                }
                else if(response.code() == 101) {
                    Global.currentUser = null
                    testTextView.visibility = View.VISIBLE
                    mainRelativeLayout.visibility = View.GONE
                }
                else if(response.code() == 500) {
                    val data = response.body()?.data

                    Toast.makeText(this@MainActivity, "$data", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<UserResponse<ArrayList<User>>>, t: Throwable) {
                Log.e("NetworkTest", "error:$t")
            }
        })
    }
}