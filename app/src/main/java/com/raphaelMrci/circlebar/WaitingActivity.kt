package com.raphaelMrci.circlebar

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.raphaelMrci.circlebar.network.SocketHandler

class WaitingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_waiting)

        val cocktailId = intent.getIntExtra(COCKTAIL_ID, 0)
        val mSocket = SocketHandler.getSocket()

        mSocket.on("ready") {
            runOnUiThread {
                val intent = Intent(this, ValidateActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        mSocket.emit("command", cocktailId)
    }
}