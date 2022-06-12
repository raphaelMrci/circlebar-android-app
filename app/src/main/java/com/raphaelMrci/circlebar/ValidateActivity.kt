package com.raphaelMrci.circlebar

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.raphaelMrci.circlebar.network.SocketHandler

class ValidateActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_validate)

        val btn = findViewById<Button>(R.id.validate_btn)
        val mSocket = SocketHandler.getSocket()

        btn.setOnClickListener {
            val intent = Intent(this, PreparingActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}