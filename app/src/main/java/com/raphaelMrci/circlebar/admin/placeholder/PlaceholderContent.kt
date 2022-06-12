package com.raphaelMrci.circlebar.admin.placeholder

import com.raphaelMrci.circlebar.models.Drink
import java.util.ArrayList
import java.util.HashMap

object PlaceholderContent {

    /**
     * An array of sample (placeholder) items.
     */
    val ITEMS: MutableList<Drink> = ArrayList()

    val ITEM_MAP: MutableMap<String, Drink> = HashMap()

    init {
            //addItem(createPlaceholderItem(i))
    }

    private fun addItem(item: Drink) {
        ITEMS.add(item)
        ITEM_MAP.put(item.id.toString(), item)
    }

    private fun makeDetails(position: Int): String {
        val builder = StringBuilder()
        builder.append("Details about Item: ").append(position)
        for (i in 0..position - 1) {
            builder.append("\nMore details information here.")
        }
        return builder.toString()
    }
}