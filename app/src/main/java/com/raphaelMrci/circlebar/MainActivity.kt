package com.raphaelMrci.circlebar

import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.raphaelMrci.circlebar.network.ApiClient
import com.raphaelMrci.circlebar.network.SocketHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

const val COCKTAIL_ID = "com.raphaelMrci.circlebar.COCKTAIL_ID"

var LOGIN_TOKEN = ""

class MainActivity : AppCompatActivity(), CoroutineScope {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val linearLayout = findViewById<LinearLayout>(R.id.scroll_content)
        val loginBtn = findViewById<Button>(R.id.sign_in_button)

        loginBtn.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        SocketHandler.setSocket("10.188.154.201")

        val mSocket = SocketHandler.getSocket()

        mSocket.connect()

        executeCall(linearLayout)
    }

    private fun executeCall(linearLayout: LinearLayout) {
        launch(Dispatchers.Main) {
            try {
                val response = ApiClient.apiService.getAvailableCocktails()

                if (response.isSuccessful && response.body() != null) {
                    val content = response.body()

                    if (content != null) {
                        for ((i, cocktail) in content.withIndex()) {
                            val button_dynamic = Button(this@MainActivity)

                            button_dynamic.layoutParams = LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                            )
                            button_dynamic.text = cocktail.name
                            button_dynamic.setOnClickListener {
                                val intent = Intent(this@MainActivity, WaitingActivity::class.java).apply {
                                    putExtra(COCKTAIL_ID, cocktail.id)
                                }
                                startActivity(intent)
                            }
                            linearLayout.addView(button_dynamic)
                        }
                    }
                } else {
                    Toast.makeText(
                        this@MainActivity,
                        "Error Occurred: ${response.message()}",
                        Toast.LENGTH_LONG
                    ).show()
                }

            } catch (e: Exception) {
                Toast.makeText(
                    this@MainActivity,
                    "Error Occurred: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    override val coroutineContext: CoroutineContext = Job()
}