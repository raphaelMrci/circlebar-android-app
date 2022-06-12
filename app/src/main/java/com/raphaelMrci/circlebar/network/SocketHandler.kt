package com.raphaelMrci.circlebar.network

import io.socket.client.IO
import io.socket.client.Socket
import java.net.URISyntaxException

// 10.188.154.201

object SocketHandler {
    lateinit var mSocket: Socket

    @Synchronized
    fun setSocket(m: String) {
        try {
            mSocket = IO.socket("http://" + m + ":3000")
        } catch (e: URISyntaxException) {
            throw e
        }
    }

    @Synchronized
    fun getSocket(): Socket {
        return mSocket
    }
}