package com.raphaelMrci.circlebar.admin.slots

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.raphaelMrci.circlebar.LOGIN_TOKEN
import com.raphaelMrci.circlebar.databinding.FragmentSlotBinding
import com.raphaelMrci.circlebar.models.Drink
import com.raphaelMrci.circlebar.models.Slot
import com.raphaelMrci.circlebar.network.ApiClient
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class MySlotsRecyclerViewAdapter(
    private val values: List<Slot>,
    private val drinks: MutableList<Drink>,
    private val mContext: Context
) : RecyclerView.Adapter<MySlotsRecyclerViewAdapter.ViewHolder>(), CoroutineScope {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            FragmentSlotBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.slotName.text = item.id.toString()
        var drinkNames: MutableList<String> = ArrayList()
        var idx = -1

        drinks.forEachIndexed { i, drink ->
            drink.name?.let { drinkNames.add(it) }
            if (drink.id == item.drink_id) {
                holder.slotContent.text = drink.name
                idx = i
            }
        }
        holder.editButton.setOnClickListener {
            AlertDialog.Builder(mContext)
                .setSingleChoiceItems(drinkNames.toTypedArray(), idx) { d, self ->
                    idx = self
                    holder.slotContent.text = drinkNames[self]
                    launch(Dispatchers.Main) {
                        try {
                            val response = item.id?.let { it1 -> ApiClient.apiService.editSlot(it1, Slot(it1, drinks[self].id), "Bearer $LOGIN_TOKEN") }

                            if (response != null && response.isSuccessful) {
                                // TODO: Toast
                            } else {
                                // TODO: Toast
                            }
                        } catch (e: Exception) {
                            // TODO: Toast
                        }
                    }
                    d.dismiss()
                }
                .setNegativeButton("Cancel") { d,_ -> d.dismiss()}
                .create().show()
        }
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: FragmentSlotBinding) : RecyclerView.ViewHolder(binding.root) {
        val slotName: TextView = binding.slotName
        val slotContent: TextView = binding.slotContent
        val editButton: Button = binding.editSlot
    }

    override val coroutineContext: CoroutineContext = Job()
}