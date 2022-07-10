package com.madcamp.project2.Game

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.madcamp.project2.databinding.ActivityGameBinding
import com.madcamp.project2.databinding.ActivityInfoBinding

class GameActivity: AppCompatActivity() {
    private val TAG: String = this.javaClass.simpleName
    private var mBinding: ActivityGameBinding? = null
    private val binding get() = mBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBinding()
    }

    private fun initBinding() {
        mBinding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}