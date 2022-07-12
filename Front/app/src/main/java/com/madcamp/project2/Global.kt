package com.madcamp.project2


import io.socket.client.Socket

object Global {
    var headers = HashMap<String, String>()
    var currentUserId: Int? = null

    // ---------------------- SOCKET ----------------------------
    const val WS_BASE_URL = "http://192.249.18.176/"
    var socket: Socket? = null
}