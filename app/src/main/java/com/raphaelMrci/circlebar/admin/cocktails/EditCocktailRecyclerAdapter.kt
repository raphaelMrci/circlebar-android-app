package com.raphaelMrci.circlebar.admin.cocktails

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.raphaelMrci.circlebar.R
import com.raphaelMrci.circlebar.models.Drink
import com.raphaelMrci.circlebar.models.RecipeItem

class EditCocktailRecyclerAdapter(private val mContext: Context, private var values: MutableList<RecipeItem>, private val drinks: MutableList<Drink>, private val recyclerView: RecyclerView) : RecyclerView.Adapter<EditCocktailRecyclerAdapter.ViewHolder>() {

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
        drinks.forEach { drink ->
            if (drink.id == newList.drink_id) {
                holder.name.text = drink.name
            }
        }
        holder.qty.setText(newList.qty.toString())
        holder.deleteBtn.setOnClickListener {
            AlertDialog.Builder(mContext)
                .setTitle("Remove ${holder.name.text} ?")
                .setMessage("Are you sure you want to remove this drink from the cocktail recipe ?")
                .setPositiveButton("Remove") { d,_ ->
                    values.removeAt(position)
                    recyclerView.adapter = EditCocktailRecyclerAdapter(mContext, values, drinks, recyclerView)
                    d.dismiss()
                }
                .setNegativeButton("Cancel") { d,_ ->
                    d.dismiss()
                }
                .create()
                .show()
        }
    }

    override fun getItemCount(): Int {
        return values.size
    }
}