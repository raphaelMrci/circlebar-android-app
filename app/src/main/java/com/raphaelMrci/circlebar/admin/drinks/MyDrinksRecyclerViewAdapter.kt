package com.raphaelMrci.circlebar.admin.drinks

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.raphaelMrci.circlebar.LOGIN_TOKEN

import com.raphaelMrci.circlebar.databinding.FragmentDrinksBinding
import com.raphaelMrci.circlebar.models.Drink
import com.raphaelMrci.circlebar.network.ApiClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class MyDrinksRecyclerViewAdapter(
    private val values: MutableList<Drink>,
    private var myContext: Context,
    private val recv: RecyclerView
) : RecyclerView.Adapter<MyDrinksRecyclerViewAdapter.ViewHolder>(), CoroutineScope {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            FragmentDrinksBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]

        holder.idView.text = item.name
        holder.editBtn.setOnClickListener {
            myContext.startActivity(Intent(myContext, EditDrinkActivity::class.java).apply {
                putExtra("name", item.name)
                putExtra("icon", item.icon)
                putExtra("id", item.id)
            })
        }
        holder.deleteBtn.setOnClickListener {
            val builder = AlertDialog.Builder(myContext)
            builder.setTitle("Delete ${item.name} ?")
            builder.setMessage("Are you sure you want to delete this drink ? Be sure it is not included on an existing cocktail before deleting it.")
            builder.setPositiveButton("Delete") { _,_ ->
                item.id?.let { it1 -> deleteDrink(it1) }
            }
            builder.setNegativeButton("Cancel") {_,_ -> }
            builder.create().show()
        }
    }

    private fun deleteDrink(id: Int) {
        launch(Dispatchers.Main) {
            try {
                val result = ApiClient.apiService.deleteDrink(id, "Bearer $LOGIN_TOKEN")

                if (result.isSuccessful) {
                    Toast.makeText(
                        myContext,
                        "Drink successfully deleted.",
                        Toast.LENGTH_SHORT
                    ).show()
                    launch(Dispatchers.Main) {
                        try {
                            val response = ApiClient.apiService.getDrinks("Bearer $LOGIN_TOKEN")

                            if (response.isSuccessful && response.body() != null) {
                                val content = response.body()

                                if (content != null) {
                                    recv.adapter = MyDrinksRecyclerViewAdapter(content, myContext, recv)
                                }
                            }
                        } catch (e: Exception) {
                            // TODO: display toast
                        }
                    }
                } else {
                    Toast.makeText(
                        myContext,
                        "Unable to delete this drink...",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch(e: Exception) {
                Toast.makeText(
                    myContext,
                    e.toString(),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: FragmentDrinksBinding) : RecyclerView.ViewHolder(binding.root) {
        val idView: TextView = binding.itemName
        val editBtn: Button = binding.editDrink
        val deleteBtn: Button = binding.deleteDrink
    }

    override val coroutineContext: CoroutineContext = Job()
}