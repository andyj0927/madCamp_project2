package com.madcamp.project2.Service

import android.content.Context
import io.socket.client.IO
import io.socket.client.Socket

class SocketManager private constructor(private var context: Context) {

    private var socket: Socket? = null
    private val WS_BASE_URL = "ws://192.249.18.176/"

    companion object {
        var instance: SocketManager? = null
        fun getInstance(context: Context): SocketManager? {
            if (instance == null) {
                instance = SocketManager(context)
            }
            return instance
        }
    }

    fun getSocket() = socket

    init {
        socket = IO.socket(WS_BASE_URL)
        socket!!.connect()
    }
}