package com.madcamp.project2.Fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.madcamp.project2.Auth.LoginActivity
import com.madcamp.project2.Auth.RegisterActivity
import com.madcamp.project2.Data.UserResponse
import com.madcamp.project2.Global
import com.madcamp.project2.Home.MainActivity
import com.madcamp.project2.R
import com.madcamp.project2.Service.ServiceCreator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class TopFragment: Fragment() {
    lateinit var loginButton: Button
    lateinit var registerButton: Button
    lateinit var myInfoButton: Button
    lateinit var logoutButton: Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view: View = inflater.inflate(R.layout.fragment_top, container, false)

        initViews(view)
        initListeners()
        Global.getCurrentUser(this.requireContext())
        setTopButtons()

        return view
    }

    private fun initViews(view: View) {
        loginButton = view.findViewById(R.id.toLoginButton)
        registerButton = view.findViewById(R.id.toRegisterButton)
        myInfoButton = view.findViewById(R.id.toMyInfoButton)
        logoutButton = view.findViewById(R.id.toLogoutButton)
    }

    private fun initListeners(){
        loginButton.setOnClickListener{
            val intent = Intent(activity, LoginActivity::class.java)
            startActivity(intent)
        }
        registerButton.setOnClickListener {
            val intent = Intent(activity, RegisterActivity::class.java)
            startActivity(intent)
        }

        logoutButton.setOnClickListener {
            val intent = Intent(activity, MainActivity::class.java)
            startActivity(intent)
            val call: Call<UserResponse> =
                ServiceCreator.userService.getLogout("application/json")

            call.enqueue(object : Callback<UserResponse> {
                override fun onResponse(
                    call: Call<UserResponse>,
                    response: Response<UserResponse>
                ) {
                    if (response.code() == 200) {
                        Global.currentUser = response.body()?.data

                        Toast.makeText(
                            activity,
                            "로그아웃",
                            Toast.LENGTH_LONG
                        ).show()
                        startActivity(Intent(activity, LoginActivity::class.java))
                    }
                    else {
                        val data: String? = response.body()?.toString()
                        Toast.makeText(activity, data, Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                    Log.e("NetworkTest", "error:$t")
                }
            })
            activity?.finish()
        }
    }

    private fun setTopButtons() {
        if(Global.currentUser != null) {
            loginButton.visibility = View.GONE
            registerButton.visibility = View.GONE
            myInfoButton.visibility = View.VISIBLE
            logoutButton.visibility = View.VISIBLE
        }

        else {
            loginButton.visibility = View.VISIBLE
            registerButton.visibility = View.VISIBLE
            myInfoButton.visibility = View.GONE
            logoutButton.visibility = View.GONE
        }
    }
}