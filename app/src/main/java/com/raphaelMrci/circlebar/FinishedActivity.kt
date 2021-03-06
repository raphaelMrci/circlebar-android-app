package com.raphaelMrci.circlebar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.raphaelMrci.circlebar.network.SocketHandler

class FinishedActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_finished)

        val mSocket = SocketHandler.getSocket()

        val retrievedBtn = findViewById<Button>(R.id.retrieved_cup_btn)

        retrievedBtn.setOnClickListener {
            mSocket.emit("removed")
        }

        mSocket.on("finished") {
            runOnUiThread {
                finish()
            }
        }
    }
}