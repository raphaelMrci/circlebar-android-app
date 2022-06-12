package com.raphaelMrci.circlebar.admin

import android.content.Context
import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity

import com.raphaelMrci.circlebar.databinding.FragmentDrinksBinding
import com.raphaelMrci.circlebar.models.Drink

class MyDrinksRecyclerViewAdapter(
    private val values: MutableList<Drink>,
    var myContext: Context
) : RecyclerView.Adapter<MyDrinksRecyclerViewAdapter.ViewHolder>() {

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
                putExtra("icon", item.name)
                putExtra("id", item.id)
            })
        }
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: FragmentDrinksBinding) : RecyclerView.ViewHolder(binding.root) {
        val idView: TextView = binding.itemName
        val editBtn: Button = binding.editDrink
        val deleteBtn: Button = binding.deleteDrink
    }

}