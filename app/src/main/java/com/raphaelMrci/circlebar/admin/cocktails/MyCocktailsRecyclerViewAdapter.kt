package com.raphaelMrci.circlebar.admin.cocktails

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.raphaelMrci.circlebar.LOGIN_TOKEN
import com.raphaelMrci.circlebar.databinding.FragmentCocktailBinding
import com.raphaelMrci.circlebar.models.Cocktail
import com.raphaelMrci.circlebar.network.ApiClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
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
            builder.setPositiveButton("Delete") { d,_ ->
                item.id?.let { it1 -> deleteCocktail(it1) }
                d.dismiss()
            }
            builder.setNegativeButton("Cancel") { d,_ ->
                d.dismiss()
            }
            builder.create().show()
        }
    }

    private fun deleteCocktail(id: Int) {
        launch(Dispatchers.Main) {
            try {
                val result = ApiClient.apiService.deleteCocktail(id, "Bearer $LOGIN_TOKEN")

                if (result.isSuccessful) {
                    Toast.makeText(
                        mContext,
                        "Cocktail successfully deleted.",
                        Toast.LENGTH_SHORT
                        //TODO: reload cocktails list
                    ).show()
                } else {
                    Toast.makeText(
                        mContext,
                        "Unable to delete this cocktail...",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch(e: Exception) {
                Toast.makeText(
                    mContext,
                    e.toString(),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
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