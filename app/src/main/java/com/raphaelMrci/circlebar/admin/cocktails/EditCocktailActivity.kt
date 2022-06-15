package com.raphaelMrci.circlebar.admin.cocktails

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.raphaelMrci.circlebar.COCKTAILS
import com.raphaelMrci.circlebar.R
import com.raphaelMrci.circlebar.models.Cocktail

class EditCocktailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_cocktail)

        val id = intent.getIntExtra("id", -1)

        val nameField = findViewById<EditText>(R.id.edit_cocktailName)
        val iconField = findViewById<EditText>(R.id.edit_cocktailIcon)
        val addNewItem = findViewById<FloatingActionButton>(R.id.edit_addRecipeItem)
        val saveCocktail = findViewById<FloatingActionButton>(R.id.edit_cocktailSave_btn)
        val recyclerView = findViewById<RecyclerView>(R.id.edit_cocktail_recyclerview)

        if (id == -1) {
            // TODO: Toast to indicate that an error occured. No id present
        }

        COCKTAILS?.forEach { cocktail ->
            if (cocktail.id == id) {
                Log.d("EDIT", "GOOD !!")
                initEditCocktail(cocktail, nameField, iconField, recyclerView)
            }
            Log.d("EDIT", "next")
        }
        Log.d("EDIT", "next steps")
    }

    private fun initEditCocktail(cocktail: Cocktail,
                                 nameField: EditText,
                                 iconField: EditText,
                                 recyclerView: RecyclerView
    ) {
        nameField.setText(cocktail.name)
        iconField.setText(cocktail.icon.toString())

        recyclerView.layoutManager = LinearLayoutManager(this@EditCocktailActivity)
        recyclerView.adapter = cocktail.recipe?.let { EditCocktailRecyclerAdapter(this, it) }
    }
}