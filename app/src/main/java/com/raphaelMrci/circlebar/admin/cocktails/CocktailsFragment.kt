package com.raphaelMrci.circlebar.admin.cocktails

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.raphaelMrci.circlebar.COCKTAILS
import com.raphaelMrci.circlebar.LOGIN_TOKEN
import com.raphaelMrci.circlebar.R
import com.raphaelMrci.circlebar.network.ApiClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class CocktailsFragment(private val mContext: Context, private val fab: FloatingActionButton) : Fragment(), CoroutineScope {

    @Deprecated("Deprecated in Java")
    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)

        if (isVisibleToUser) {
            fab.show()
            fab.setOnClickListener {
                mContext.startActivity(Intent(mContext, NewCocktailActivity::class.java))
            }
        }
        if (view is RecyclerView) {
            getCocktails(view as RecyclerView)
        } else {
            Log.w("COCKTAIL", "Cocktail view is not a RecyclerView")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_cocktail_list, container, false)

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = LinearLayoutManager(context)
                getCocktails(view)
            }
        }
        return view
    }

    private fun getCocktails(view: RecyclerView) {
        launch(Dispatchers.Main) {
            try {
                val response = ApiClient.apiService.getCocktails("Bearer $LOGIN_TOKEN")

                if (response.isSuccessful && response.body() != null) {
                    COCKTAILS = response.body()

                    if (COCKTAILS != null) {
                        view.adapter = MyCocktailsRecyclerViewAdapter(COCKTAILS!!, mContext)
                    }
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

    companion object {
        @JvmStatic
        fun newInstance(mContext: Context, fab: FloatingActionButton) = CocktailsFragment(mContext, fab)
    }

    override val coroutineContext: CoroutineContext = Job()
}