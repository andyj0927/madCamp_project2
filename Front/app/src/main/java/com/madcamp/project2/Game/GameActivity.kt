package com.madcamp.project2.Game

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import com.madcamp.project2.Data.Card
import com.madcamp.project2.Global
import com.madcamp.project2.databinding.ActivityGameBinding
import io.socket.client.IO
import io.socket.client.Socket

class GameActivity: AppCompatActivity() {
    private val URL = Global.WS_BASE_URL + "gameRoom/"
    private var socket: Socket? = null
    private val TAG: String = this.javaClass.simpleName
    private var mBinding: ActivityGameBinding? = null
    private val binding get() = mBinding!!
    private lateinit var cardAdapter: CardAdapter
    private lateinit var list: MutableList<Card>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        initSocket()
        initBinding()
        initVars()
        initRecyclerView()
    }

    private fun initSocket() {
        socket = IO.socket(URL)
        socket?.connect()
    }

    private fun initBinding() {
        mBinding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun initVars() {
        list = mutableListOf()

        val mList: MutableList<Int> = mutableListOf()

        while(mList.size < 16) {
            val num: Int = (Math.random() * 50 + 1).toInt()
            var flag = true

            for(i in 0 until mList.size) {
                if(num == mList[i]) {
                    flag = false
                    break
                }
            }
            if(flag) {
                mList.add(num)
                mList.add(num)
            }
        }

        mList.shuffle()

        for(i in 0 until mList.size) {
            list.add(Card(i, mList[i], false))
        }

        Log.d(TAG, list.toString())
        cardAdapter = CardAdapter(list)
    }

    private fun flip(pos1: Int, pos2: Int) {
        list[pos1].status = !(list[pos1].status)
        list[pos2].status = !(list[pos2].status)

        cardAdapter.notifyItemChanged(pos1)
        cardAdapter.notifyItemChanged(pos2)
    }

    private fun initRecyclerView() {
        val gridLayoutManager = GridLayoutManager(this@GameActivity, 4)

        binding.cardRecyclerView.layoutManager = gridLayoutManager
        binding.cardRecyclerView.adapter = cardAdapter
    }
}