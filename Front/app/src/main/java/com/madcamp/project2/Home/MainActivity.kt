package com.madcamp.project2.Home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.madcamp.project2.Data.ResponseType
import com.madcamp.project2.Data.User
import com.madcamp.project2.Global
import com.madcamp.project2.R
import com.madcamp.project2.Service.ServiceCreator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    lateinit var list: ArrayList<User>
    lateinit var recyclerViewAdapter: RecyclerViewAdapter
    lateinit var testTextView: TextView
    lateinit var recview: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        list = ArrayList()
        initViews()
        getUserList()
    }

    private fun initViews() {
        recview =findViewById(R.id.rc_view)
        testTextView = findViewById(R.id.testTextView)
    }

    private fun getUserList() {
        val call: Call<ResponseType<ArrayList<User>>> =
            ServiceCreator.userService.getUserList(Global.headers)

        call.enqueue(object : Callback<ResponseType<ArrayList<User>>> {
            override fun onResponse(
                call: Call<ResponseType<ArrayList<User>>>,
                response: Response<ResponseType<ArrayList<User>>>
            ) {
                if (response.code() == 200) {
                    list = response.body()?.data?: ArrayList()
                    Log.d("userList", list.toString())

                    recyclerViewAdapter = RecyclerViewAdapter(list)
                    recview.adapter = recyclerViewAdapter

                    recview.visibility = View.VISIBLE
                    testTextView.visibility = View.GONE
                }
                else if(response.code() == 101) {
                    Global.currentUserId = null
                    testTextView.visibility = View.VISIBLE
                    recview.visibility = View.GONE
                }
                else if(response.code() == 500) {
                    val message = response.body()?.message

                    Toast.makeText(this@MainActivity, "$message", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<ResponseType<ArrayList<User>>>, t: Throwable) {
                Log.e("NetworkTest", "error:$t")
            }
        })
    }
}