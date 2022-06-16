package com.raphaelMrci.circlebar.admin.cocktails

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.raphaelMrci.circlebar.LOGIN_TOKEN
import com.raphaelMrci.circlebar.R
import com.raphaelMrci.circlebar.models.Cocktail
import com.raphaelMrci.circlebar.models.Drink
import com.raphaelMrci.circlebar.models.RecipeItem
import com.raphaelMrci.circlebar.network.ApiClient
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class NewCocktailActivity : AppCompatActivity(), CoroutineScope {
    private var newCocktail = Cocktail(recipe = ArrayList())
    private lateinit var drinks: MutableList<Drink>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_cocktail)

        val nameField = findViewById<EditText>(R.id.new_cocktailName)
        val iconField = findViewById<EditText>(R.id.new_cocktailIcon)
        val addNewItem = findViewById<FloatingActionButton>(R.id.new_addRecipeItem)
        val saveCocktail = findViewById<FloatingActionButton>(R.id.new_cocktailSave_btn)
        val recyclerView = findViewById<RecyclerView>(R.id.new_cocktail_recyclerview)

        recyclerView.layoutManager = LinearLayoutManager(this)

        launch(Dispatchers.Main) {
            try {
                val response = ApiClient.apiService.getDrinks("Bearer $LOGIN_TOKEN")

                if (response.isSuccessful && response.body() != null) {
                    drinks = response.body()!!
                } else {
                    // TODO: display taost
                }
            } catch (e: Exception) {
                // TODO: display toast
            }
        }

        addNewItem.setOnClickListener {
            val availableItemNames: MutableList<String> = ArrayList()
            val availableItemIds: MutableList<Int> = ArrayList()

            drinks.forEachIndexed { i, drink ->
                var isAvailable = true
                newCocktail.recipe?.forEach { item ->
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

                        newCocktail.recipe?.add(RecipeItem(curDrink.id, 0))
                        Log.d("ADD", curDrink.toString())
                        recyclerView.adapter =
                            newCocktail.recipe?.let { it1 ->
                                EditCocktailRecyclerAdapter(this,
                                    it1, drinks, recyclerView)
                            }
                        d.dismiss()
                    }
                }
                .setNegativeButton("Cancel") { d,_ -> d.dismiss()}
                .create().show()
        }

        saveCocktail.setOnClickListener {
            launch(Dispatchers.Main) {
                try {
                    newCocktail.name = nameField.text.toString()
                    newCocktail.icon = iconField.text.toString().toInt()
                    Log.d("ADD", newCocktail.recipe.toString())
                    val response =
                        newCocktail.let { it1 ->
                            ApiClient.apiService.createCocktail(newCocktail, "Bearer $LOGIN_TOKEN")
                        }

                    if (response.isSuccessful) {
                        Toast.makeText(
                            this@NewCocktailActivity,
                            "Cocktail successfully created.",
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
                    }
                } catch (e: Exception) {
                    Toast.makeText(
                        this@NewCocktailActivity,
                        "Error occurred : ${e.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    override val coroutineContext: CoroutineContext = Job()
}