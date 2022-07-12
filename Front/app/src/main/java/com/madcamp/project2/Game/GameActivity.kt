package com.madcamp.project2.Game

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.madcamp.project2.Data.Card
import com.madcamp.project2.Data.ResponseType
import com.madcamp.project2.Data.User
import com.madcamp.project2.Global
import com.madcamp.project2.Home.MainActivity
import com.madcamp.project2.Home.RecyclerViewClickListener
import com.madcamp.project2.R
import com.madcamp.project2.Service.ServiceCreator
import com.madcamp.project2.databinding.ActivityGameBinding
import kotlinx.coroutines.*
import java.io.IOException

class GameActivity: AppCompatActivity() {
    private val TAG: String = this.javaClass.simpleName
    private var mBinding: ActivityGameBinding? = null
    private val binding get() = mBinding!!
    private lateinit var listener: RecyclerViewClickListener
    private lateinit var cardAdapter: CardAdapter
    private lateinit var list: MutableList<Card>
    private var c_score: Int = 0
    private var d_score: Int = 0
    private var c_id: Int = -1
    private var d_id: Int = -1
    private var c_socket_id: String = ""
    private var d_socket_id: String = ""
    private var pos1: Int = -1
    private var pos2: Int = -1
    private var myTurn: Boolean = false
    private var pair: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initVars()
        initBinding()
        initViews()
        initExtras()
        initListener()
        initSocket()
        CoroutineScope(Dispatchers.Main).launch {
            CoroutineScope(Dispatchers.Default).async{
                Global.socket?.emit("connectRoom", c_id, d_id)
                delay(1000)
            }.await()
            if(Global.currentUserId == c_id) {
                initChallengerVars()
            }
            else {
                Log.d(TAG, "before giveMeAnArray $c_socket_id : $d_socket_id")
                Global.socket?.emit("giveMeAnArray", c_socket_id)
                myTurn = true
            }
        }

        Log.d(TAG, "onCreate ${Global.currentUserId} : ${c_id} : ${d_id}")


    }

    private fun initVars() {
        list = mutableListOf()
    }

    private fun initBinding() {
        mBinding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun initViews() {
        binding.ChallengerScore.text = "0"
        binding.DefenderScore.text = "0"

        binding.ChallengerText.setBackgroundResource(R.drawable.black_border)
        binding.DefenderText.setBackgroundResource(R.drawable.red_border)
    }

    private fun initExtras() {
        c_id = intent.getIntExtra("c_id", -1)
        d_id = intent.getIntExtra("d_id", -1)
    }

    private fun initListener() {
        listener = object: RecyclerViewClickListener {
            override fun onClick(view: View, position: Int) {
                if(!myTurn) return

                if(Global.currentUserId == c_id) {
                    Global.socket?.emit("playingGame", d_socket_id, position)
                } else {
                    Global.socket?.emit("playingGame", c_socket_id, position)
                }
                flipCard(position)


                if(pos2 != -1 && (list[pos1].num != list[pos2].num)) {
                    val handler = Handler(Looper.getMainLooper())
                    handler.postDelayed({
                        myTurn = false
                        flipBack()
                    }, 1000)

                    if(Global.currentUserId == d_id) {
                        binding.ChallengerText.setBackgroundResource(R.drawable.red_border)
                        binding.DefenderText.setBackgroundResource(R.drawable.black_border)
                    } else {
                        binding.ChallengerText.setBackgroundResource(R.drawable.black_border)
                        binding.DefenderText.setBackgroundResource(R.drawable.red_border)
                    }
                }

                if(pos2 != -1 && (list[pos1].num == list[pos2].num)) {
                    myTurn = true
                    pos1 = -1
                    pos2 = -1

                    ++pair
                    if(Global.currentUserId == c_id) {
                        ++c_score
                        binding.ChallengerScore.text = c_score.toString()
                    }
                    else {
                        ++d_score
                        binding.DefenderScore.text = d_score.toString()
                    }

                    if(pair == 8) {
                        var cIsWin = 0
                        if(c_score > d_score) cIsWin = 1
                        if(c_score < d_score) cIsWin = -1
                        if(c_score == d_score) cIsWin = 0

                        Global.socket?.emit("GameSet", c_id, d_id, cIsWin)

                        showModal()
                    }
                }
            }
        }
    }

    private fun initSocket() {
        Global.socket?.on("socketInfo") {
            c_socket_id = it!![0] as String
            d_socket_id = it[1] as String

            Log.d(TAG, "$c_socket_id : $d_socket_id")
        }

        Global.socket?.on("requestArray") {
            val gson = Gson()
            val jsonString = gson.toJson(list)
            Log.d(TAG, "before Send; list: $jsonString")
            Global.socket?.emit("arrayIsHere", d_socket_id, jsonString)
        }

        Global.socket?.on("responseArray") {
            val jsonString = it!![0] as String
            val gson = Gson()
            list = gson.fromJson(jsonString, object: TypeToken<MutableList<Card>>(){}.type)

            Log.d(TAG, "After Receive: $list")
            initRecyclerView()

            myTurn = true
        }

        Global.socket?.on("clickedCardPosition") {
            val pos = it!![0] as Int

            flipCard(pos)

            val handler = Handler(Looper.getMainLooper())
            if(pos2 != -1 && (list[pos1].num != list[pos2].num)) {
                myTurn = true
                handler.postDelayed({
                        flipBack()
                }, 1000)

                if(Global.currentUserId == c_id) {
                    binding.ChallengerText.setBackgroundResource(R.drawable.red_border)
                    binding.DefenderText.setBackgroundResource(R.drawable.black_border)
                } else {
                    binding.ChallengerText.setBackgroundResource(R.drawable.black_border)
                    binding.DefenderText.setBackgroundResource(R.drawable.red_border)
                }
            }

            if(pos2 != -1 && (list[pos1].num == list[pos2].num)) {
                myTurn = false
                pos1 = -1
                pos2 = -1
                ++pair
                if(Global.currentUserId == c_id) {
                    ++d_score
                    binding.DefenderScore.text = d_score.toString()
                }
                else {
                    ++c_score
                    binding.ChallengerScore.text = c_score.toString()
                }

                if(pair == 8) {
                    showModal()
                }
            }
        }
    }

    private fun flipCard(position: Int) {
        if((pos1 != -1 && pos2 != -1) || list[position].status) return

        if(pos1 == -1) pos1 = position
        else if(pos2 == -1) pos2 = position
        list[position].status = true

        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            cardAdapter.notifyItemChanged(position)
        }, 0)
    }

    private fun flipBack() {
        list[pos1].status = false
        list[pos2].status = false

        val handler = Handler(Looper.getMainLooper())

        handler.postDelayed({
            cardAdapter.notifyItemChanged(pos1)
            cardAdapter.notifyItemChanged(pos2)

            pos1 = -1
            pos2 = -1
        }, 0)
    }

    // init Challenger's --------------------------------------------------------------
    private fun initChallengerVars() {
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
        initRecyclerView()
    }

    private fun initRecyclerView() {
        val gridLayoutManager = GridLayoutManager(this@GameActivity, 4)

        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            cardAdapter = CardAdapter(list, listener)
            binding.cardRecyclerView.layoutManager = gridLayoutManager
            binding.cardRecyclerView.adapter = cardAdapter
            cardAdapter.notifyDataSetChanged()
        }, 0)
    }

    private fun showModal() {
        val alertDialogBuilder: AlertDialog.Builder = this.let {
            AlertDialog.Builder(this)
        }

        alertDialogBuilder.setTitle("Game Set")
        val result = if(c_score > d_score) {
            "Challenger Win!!"
        } else if (c_score < d_score) {
            "Defender Win!!"
        } else {
            "Draw"
        }

        alertDialogBuilder.setMessage("Challenger's score: $c_score \nDefender's score: ${d_score}\n ${result}")
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            alertDialogBuilder.apply {
                setPositiveButton("나가기") { dialog, id ->
                    try {
                        Log.d(TAG, "Game set")
                        val intent = Intent(this@GameActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }.create()?.show()
        }, 0)
    }

}