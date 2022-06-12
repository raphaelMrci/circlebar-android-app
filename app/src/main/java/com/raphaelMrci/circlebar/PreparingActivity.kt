package com.raphaelMrci.circlebar

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ProgressBar

class PreparingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preparing)

        val mSocket = SocketHandler.getSocket()

        mSocket.on("get-cup") {
            runOnUiThread {
                val intent = Intent(this, FinishedActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
        mSocket.emit("ready")
    }
}
