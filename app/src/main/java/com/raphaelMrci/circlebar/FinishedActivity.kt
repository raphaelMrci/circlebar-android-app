package com.raphaelMrci.circlebar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.raphaelMrci.circlebar.network.SocketHandler

class FinishedActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_finished)

        val mSocket = SocketHandler.getSocket()

        mSocket.on("finished") {
            runOnUiThread {
                finish()
            }
        }
    }
}