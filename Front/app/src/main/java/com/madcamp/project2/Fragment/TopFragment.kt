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
            val call: Call<UserResponse<Unit>> =
                ServiceCreator.userService.getLogout()

            call.enqueue(object : Callback<UserResponse<Unit>> {
                override fun onResponse(
                    call: Call<UserResponse<Unit>>,
                    response: Response<UserResponse<Unit>>
                ) {
                    if (response.code() == 200) {
                        Global.currentUser = null
                        Global.token = null
                        setTopButtons()

                        Log.d("currentUser", Global.currentUser.toString())
                        Toast.makeText(activity, "Logout Success", Toast.LENGTH_LONG).show()

                        val intent = Intent(activity, MainActivity::class.java)
                        startActivity(intent)
                    }
                    else if (response.code() == 401) {
                        Toast.makeText(activity, "Failed to Logout", Toast.LENGTH_LONG).show()
                    }
                    else {
                        Toast.makeText(activity, "${response.code()}", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<UserResponse<Unit>>, t: Throwable) {
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