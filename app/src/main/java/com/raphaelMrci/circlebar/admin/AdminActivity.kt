package com.raphaelMrci.circlebar.admin

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnFocusChangeListener
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.viewpager.widget.ViewPager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.raphaelMrci.circlebar.LOGIN_TOKEN
import com.raphaelMrci.circlebar.R
import com.raphaelMrci.circlebar.databinding.ActivityAdminBinding
import com.raphaelMrci.circlebar.models.Drink
import com.raphaelMrci.circlebar.models.Login
import com.raphaelMrci.circlebar.network.ApiClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext


class AdminActivity : AppCompatActivity(), CoroutineScope {

    private lateinit var binding: ActivityAdminBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fab: FloatingActionButton = binding.fab
        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager, fab)
        val viewPager: ViewPager = binding.viewPager
        val adminSettings = binding.userSettingsButton
        val tabs: TabLayout = binding.tabs
        val inflater = LayoutInflater.from(this)

        viewPager.adapter = sectionsPagerAdapter
        tabs.setupWithViewPager(viewPager)

        adminSettings.setOnClickListener {
            val v = inflater.inflate(R.layout.dialog_userdata, null)
            val username = v.findViewById<EditText>(R.id.username_input)
            val password = v.findViewById<EditText>(R.id.password_input)

            launch(Dispatchers.Main) {
                try {
                    val response = ApiClient.apiService.getAdmin("Bearer $LOGIN_TOKEN")

                    if (response.isSuccessful) {
                        val content = response.body()

                        if (content != null) {
                            username.setText(content.username)
                        }
                        val dialog = AlertDialog.Builder(this@AdminActivity)
                            .setView(v)
                            .setPositiveButton("Apply") { d,_ ->
                                if (username.text.toString() != "" && password.text.toString() != "") {
                                    editUserinfo(username, password, d)
                                } else {
                                    Toast.makeText(
                                        this@AdminActivity,
                                        "Hum...",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }
                            .setNegativeButton("Cancel") { d,_ ->
                                d.dismiss()
                            }
                            .create()
                        dialog.show()

                        dialog.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = false

                        username.addTextChangedListener {
                            dialog.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = username.text.toString() != "" && password.text.toString() != ""
                        }

                        password.addTextChangedListener {
                            dialog.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = username.text.toString() != "" && password.text.toString() != ""
                        }
                    } else {
                        finish()
                    }
                } catch (e: Exception) {
                    Toast.makeText(
                        this@AdminActivity,
                        "Error Occurred: ${e.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun editUserinfo(username: EditText, password: EditText, d: DialogInterface) {
        launch(Dispatchers.Main) {
            try {
                val response1 = ApiClient.apiService.editAdmin(Login(username.text.toString(), password.text.toString()), "Bearer $LOGIN_TOKEN")

                if (response1.isSuccessful) {
                    Toast.makeText(
                        this@AdminActivity,
                        "User info successfully edited.",
                        Toast.LENGTH_SHORT
                    ).show()
                    LOGIN_TOKEN = ""
                    d.dismiss()
                    finish()
                } else {
                    Toast.makeText(
                        this@AdminActivity,
                        "Unable to change user info...",
                        Toast.LENGTH_LONG
                    ).show()
                }
            } catch (e: Exception) {
                Toast.makeText(
                    this@AdminActivity,
                    "Error Occurred: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    override val coroutineContext: CoroutineContext = Job()
}
