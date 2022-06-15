package com.raphaelMrci.circlebar.admin.drinks

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.raphaelMrci.circlebar.LOGIN_TOKEN
import com.raphaelMrci.circlebar.R
import com.raphaelMrci.circlebar.models.Drink
import com.raphaelMrci.circlebar.network.ApiClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class EditDrinkActivity : AppCompatActivity(), CoroutineScope {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_drink)

        val nameField = findViewById<EditText>(R.id.editTextDrinkName)
        val iconfield = findViewById<EditText>(R.id.editTextDrinkIcon)
        val submitEditBtn = findViewById<Button>(R.id.editDrinkSubmitBtn)

        val id = intent.getIntExtra("id", 0)
        val name = intent.getStringExtra("name") as String
        val icon = intent.getIntExtra("icon", 0)

        nameField.setText(name)
        iconfield.setText(icon.toString())

        submitEditBtn.setOnClickListener {
            val newName = nameField.text.toString()
            val newIcon = iconfield.text.toString()

            editDrink(id, newName, newIcon.toInt())
        }
    }

    private fun editDrink(id: Int, name: String, icon: Int) {
        try {
            launch(Dispatchers.Main) {
                val drink = Drink(name, icon, id)
                val response = ApiClient.apiService.editDrink(id, drink, "Bearer $LOGIN_TOKEN")

                if (response.isSuccessful && response.body() != null) {
                    Toast.makeText(
                        this@EditDrinkActivity,
                        "Drink successfully editted.",
                        Toast.LENGTH_SHORT
                    ).show()
                    finish()
                }
            }
        } catch (e: Exception) {

        }
    }

    override val coroutineContext: CoroutineContext = Job()
}