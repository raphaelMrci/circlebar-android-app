package com.raphaelMrci.circlebar.admin.cocktails

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.raphaelMrci.circlebar.databinding.FragmentCocktailBinding
import com.raphaelMrci.circlebar.models.Cocktail
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

class MyCocktailsRecyclerViewAdapter(
    private val values: MutableList<Cocktail>,
    private var mContext: Context
) : RecyclerView.Adapter<MyCocktailsRecyclerViewAdapter.ViewHolder>(), CoroutineScope {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            FragmentCocktailBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]

        holder.cocktailName.text = item.name
        holder.editBtn.setOnClickListener {
            mContext.startActivity(Intent(mContext, EditCocktailActivity::class.java).apply {
                putExtra("id", item.id)
            })
        }
        holder.deleteBtn.setOnClickListener {
            val builder = AlertDialog.Builder(mContext)
            builder.setTitle("Delete ${item.name} ?")
            builder.setMessage("Are you sure you want to delete this cocktail ?")
            builder.setPositiveButton("Supprimer") { _,_ ->
                item.id?.let { it1 -> deleteCocktail(it1) }
            }
            builder.setNegativeButton("Annuler") {_,_ -> }
            builder.create().show()
        }
    }

    private fun deleteCocktail(id: Int) {

    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: FragmentCocktailBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val cocktailName: TextView = binding.cocktailName
        val editBtn: Button = binding.editCocktailBtn
        val deleteBtn: Button = binding.deleteCocktailBtn
    }

    override val coroutineContext: CoroutineContext = Job()
}