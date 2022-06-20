package com.raphaelMrci.circlebar.admin.drinks

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.raphaelMrci.circlebar.LOGIN_TOKEN
import com.raphaelMrci.circlebar.R
import com.raphaelMrci.circlebar.models.Drink
import com.raphaelMrci.circlebar.network.ApiClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class DrinksFragment(private val mContext: Context, private val fab: FloatingActionButton) : Fragment(), CoroutineScope {

    @Deprecated("Deprecated in Java")
    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)

        if (isVisibleToUser) {
            val inflater = LayoutInflater.from(mContext)

            fab.show()
            fab.setOnClickListener {
                val v = inflater.inflate(R.layout.dialog_add_drink, null)
                val name = v.findViewById<EditText>(R.id.add_drink_name_input)
                val icon = v.findViewById<EditText>(R.id.add_drink_icon_input)

                val dialog = AlertDialog.Builder(mContext)
                        .setView(v)
                        .setPositiveButton("Add") { d,_ ->
                            if (name.text.toString() != "" && icon.text.toString() != "") {
                                insertNewDrink(Drink(name.text.toString(), icon.text.toString().toInt()))
                            }
                            d.dismiss()
                        }
                        .setNegativeButton("Cancel") { d,_ ->
                            d.dismiss()
                        }
                        .create()
                dialog.show()

                dialog.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = false

                name.addTextChangedListener {
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = name.text.toString() != "" && icon.text.toString() != ""
                }

                icon.addTextChangedListener {
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = name.text.toString() != "" && icon.text.toString() != ""
                }
            }
            if (view is RecyclerView) {
                getDrinks(view as RecyclerView)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_drinks_list, container, false)
        if (view is RecyclerView) {
            with(view) {
                layoutManager = LinearLayoutManager(context)
                getDrinks(view)
            }
        }
        return view
    }

    private fun insertNewDrink(newDrink: Drink) {
        launch(Dispatchers.Main) {
            try {
                val response = ApiClient.apiService.createDrink(newDrink, "Bearer $LOGIN_TOKEN")

                if (response.isSuccessful) {
                    Toast.makeText(
                        mContext,
                        "Drink successfully added.",
                        Toast.LENGTH_SHORT
                    ).show()
                    if (view is RecyclerView) {
                        getDrinks(view as RecyclerView)
                    } else {
                        Log.w("DRINK", "ERROOOR !! It's not a RecyclerView !")
                    }
                } else {
                    Toast.makeText(
                        mContext,
                        "Unable to create drink...",
                        Toast.LENGTH_LONG
                    ).show()
                }
            } catch (e: Exception) {
                Toast.makeText(
                    mContext,
                    "Error Occurred: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun getDrinks(view: RecyclerView) {
        launch(Dispatchers.Main) {
            try {
                val response = ApiClient.apiService.getDrinks("Bearer $LOGIN_TOKEN")

                if (response.isSuccessful && response.body() != null) {
                    val content = response.body()

                    if (content != null) {
                        view.adapter = MyDrinksRecyclerViewAdapter(content, mContext, view)
                    }
                }
            } catch (e: Exception) {
                Toast.makeText(
                    mContext,
                    "Error Occurred: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(mContext: Context, fab: FloatingActionButton) = DrinksFragment(mContext, fab)
    }

    override val coroutineContext: CoroutineContext = Job()
}