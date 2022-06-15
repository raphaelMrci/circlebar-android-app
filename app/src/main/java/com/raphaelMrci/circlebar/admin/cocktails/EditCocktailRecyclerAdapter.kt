package com.raphaelMrci.circlebar.admin.cocktails

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.raphaelMrci.circlebar.R
import com.raphaelMrci.circlebar.databinding.FragmentDrinksBinding
import com.raphaelMrci.circlebar.models.RecipeItem

class EditCocktailRecyclerAdapter(private val mContext: Context, private val values: MutableList<RecipeItem>) : RecyclerView.Adapter<EditCocktailRecyclerAdapter.ViewHolder>() {

    inner class ViewHolder(private val v: View) : RecyclerView.ViewHolder(v) {
        val name: TextView = v.findViewById(R.id.recipe_item_name)
        val qty: EditText = v.findViewById(R.id.recipe_qty_input)
        val deleteBtn: Button = v.findViewById(R.id.recipe_delete_btn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val v = inflater.inflate(R.layout.fragment_recipes_item, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val newList = values[position]
        // TODO: get drinks list from API & get name of drink
        holder.name.text = newList.drink_id.toString()
        holder.qty.setText(newList.qty.toString())
        holder.deleteBtn.setOnClickListener {
            // TODO: delete recipe item locally
        }
    }

    override fun getItemCount(): Int {
        return values.size
    }
}