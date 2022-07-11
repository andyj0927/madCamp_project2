package com.madcamp.project2.Home

import android.content.Context
import android.content.Intent
import android.icu.text.IDNA
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.madcamp.project2.Auth.InfoActivity
import com.madcamp.project2.Data.ResponseType
import com.madcamp.project2.Data.User
import com.madcamp.project2.Global
import com.madcamp.project2.R
import com.madcamp.project2.Service.ServiceCreator
import io.socket.client.IO
import io.socket.client.Socket
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.Exception
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    private val TAG = this::class.java.simpleName
    lateinit var list: MutableList<User>
    lateinit var recyclerViewAdapter: RecyclerViewAdapter
    lateinit var testTextView: TextView
    lateinit var recview: RecyclerView
    lateinit var listener: RecyclerViewClickListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        list = mutableListOf()
        Global.initSocket(this@MainActivity)
        initViews()
        initRecyclerViewListener()
        initRefresh()
        getUserList()
    }

    private fun initViews() {
        recview = findViewById(R.id.rc_view)
        testTextView = findViewById(R.id.testTextView)
    }

    private fun initRefresh() {
        val refreshList: SwipeRefreshLayout = findViewById(R.id.refreshList)
        refreshList.setOnRefreshListener {
            val thread = Thread {
                getUserList()
            }
            thread.start()
            try {
                thread.join()
                recyclerViewAdapter.notifyDataSetChanged()
                refreshList.isRefreshing = false
            } catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

    private fun initRecyclerViewListener() {
        listener = object : RecyclerViewClickListener {
            override fun onClick(view: View, position: Int) {
                val intent = Intent(this@MainActivity, InfoActivity::class.java)
                intent.putExtra("id", list[position].id)
                startActivity(intent)
            }
        }
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
                    Log.d(TAG, "UserList: ${list}")

                    for (i in 0 until list.size) {
                        Log.d(TAG, "${list[i].id} : ${Global.currentUserId}")
                        if(list[i].id == Global.currentUserId) {
                            Log.d(TAG, "currentUserId: ${i.toString()}")
                            list.removeAt(i)
                            break
                        }
                    }

                    list.sortByDescending { it.currentlyActive }
                    Log.d("userList", list.toString())
                    recyclerViewAdapter = RecyclerViewAdapter(list, listener)
                    recview.adapter = recyclerViewAdapter

                    val manager = LinearLayoutManager(this@MainActivity)
                    manager.orientation = LinearLayoutManager.VERTICAL
                    recview.layoutManager = manager

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

    fun getContext(): Context {
        return this.getContext()
    }
}