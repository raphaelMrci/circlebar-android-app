package com.raphaelMrci.circlebar.admin.cocktails

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.raphaelMrci.circlebar.COCKTAILS
import com.raphaelMrci.circlebar.LOGIN_TOKEN
import com.raphaelMrci.circlebar.R
import com.raphaelMrci.circlebar.models.Cocktail
import com.raphaelMrci.circlebar.network.ApiClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class EditCocktailActivity : AppCompatActivity(), CoroutineScope {
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
                nameField.setText(cocktail.name)
                iconField.setText(cocktail.icon.toString())
                initRecycler(recyclerView, cocktail, this)
            }
        }
    }

    private fun initRecycler(recyclerView: RecyclerView, cocktail: Cocktail, mContext: Context) {
        launch(Dispatchers.Main) {
            try {
                val response = ApiClient.apiService.getDrinks("Bearer $LOGIN_TOKEN")

                if (response.isSuccessful && response.body() != null) {
                    recyclerView.layoutManager = LinearLayoutManager(mContext)
                    recyclerView.adapter = cocktail.recipe?.let { EditCocktailRecyclerAdapter(mContext, it,
                        response.body()!!
                    ) }
                }
            } catch (e: Exception) {
                // TODO: print toast
            }
        }
    }

    override val coroutineContext: CoroutineContext = Job()
}