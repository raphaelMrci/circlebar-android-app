package com.raphaelMrci.circlebar.admin.cocktails

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.raphaelMrci.circlebar.COCKTAILS
import com.raphaelMrci.circlebar.LOGIN_TOKEN
import com.raphaelMrci.circlebar.R
import com.raphaelMrci.circlebar.models.Cocktail
import com.raphaelMrci.circlebar.models.Drink
import com.raphaelMrci.circlebar.models.RecipeItem
import com.raphaelMrci.circlebar.network.ApiClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class EditCocktailActivity : AppCompatActivity(), CoroutineScope {
    private var newCocktail: Cocktail? = null
    private lateinit var drinks: MutableList<Drink>

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
            Toast.makeText(
                this,
                "Unable to edit cocktail...",
                Toast.LENGTH_LONG
            ).show()
        }

        addNewItem.setOnClickListener {
            val availableItemNames: MutableList<String> = ArrayList()
            val availableItemIds: MutableList<Int> = ArrayList()

            drinks.forEachIndexed { i, drink ->
                var isAvailable = true
                newCocktail?.recipe?.forEach { item ->
                    if (item.drink_id == drink.id) {
                        isAvailable = false
                    }
                }
                if (isAvailable) {
                    drink.name?.let { it1 -> availableItemNames.add(it1) }
                    availableItemIds.add(i)
                }
            }
            var selected = -1

            AlertDialog.Builder(this)
                .setSingleChoiceItems(availableItemNames.toTypedArray(), selected) { _, self ->
                    selected = self
                }
                .setPositiveButton("Add") { d,_ ->
                    if (selected != -1) {
                        val curDrink = drinks[availableItemIds[selected]]

                        newCocktail?.recipe?.add(RecipeItem(curDrink.id, 0))
                        Log.d("EDIT", newCocktail?.recipe.toString())
                        recyclerView.adapter =
                            newCocktail?.recipe?.let { it1 ->
                                EditCocktailRecyclerAdapter(this,
                                    it1, drinks, recyclerView)
                            }
                        d.dismiss()
                    }
                }
                .setNegativeButton("Cancel") { d,_ -> d.dismiss()}
                .create().show()
        }

        COCKTAILS?.forEach { cocktail ->
            if (cocktail.id == id) {
                nameField.setText(cocktail.name)
                iconField.setText(cocktail.icon.toString())
                initRecycler(recyclerView, cocktail, this)
            }
        }

        saveCocktail.setOnClickListener {
            launch {
                try {
                    newCocktail?.name = nameField.text.toString()
                    newCocktail?.icon = iconField.text.toString().toInt()
                    Log.d("EDIT", newCocktail?.recipe.toString())
                    val response =
                        newCocktail?.let { it1 ->
                            ApiClient.apiService.editCocktail(id, it1, "Bearer $LOGIN_TOKEN")
                        }

                    if (response != null && response.isSuccessful) {
                        Toast.makeText(
                            this@EditCocktailActivity,
                            "Cocktail successfully edited.",
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
                    }
                } catch (e: Exception) {
                    Toast.makeText(
                        this@EditCocktailActivity,
                        "Error occurred : ${e.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun initRecycler(recyclerView: RecyclerView, cocktail: Cocktail, mContext: Context) {
        launch(Dispatchers.Main) {
            try {
                val response = ApiClient.apiService.getDrinks("Bearer $LOGIN_TOKEN")

                if (response.isSuccessful && response.body() != null) {
                    drinks = response.body()!!
                    newCocktail = Cocktail(cocktail.name, cocktail.id, cocktail.icon, ArrayList())
                    recyclerView.layoutManager = LinearLayoutManager(mContext)
                    recyclerView.adapter = cocktail.recipe?.let {
                        newCocktail!!.recipe?.addAll(it)
                        newCocktail!!.recipe?.let { it1 ->
                            EditCocktailRecyclerAdapter(mContext,
                                it1, response.body()!!, recyclerView)
                        }
                    }
                }
            } catch (e: Exception) {
                Toast.makeText(
                    this@EditCocktailActivity,
                    "Error occurred : ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    override val coroutineContext: CoroutineContext = Job()
}