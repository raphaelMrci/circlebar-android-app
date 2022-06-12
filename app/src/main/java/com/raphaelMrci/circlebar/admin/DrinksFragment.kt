package com.raphaelMrci.circlebar.admin

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.raphaelMrci.circlebar.LOGIN_TOKEN
import com.raphaelMrci.circlebar.R
import com.raphaelMrci.circlebar.admin.placeholder.PlaceholderContent
import com.raphaelMrci.circlebar.network.ApiClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

/**
 * A fragment representing a list of Items.
 */
class DrinksFragment(private var mContext: Context) : Fragment(), CoroutineScope {

    private var columnCount = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_drinks_list, container, false)

        if (view is RecyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                getDrinks(view)
            }
        }
        return view
    }

    private fun getDrinks(view: RecyclerView) {
        launch(Dispatchers.Main) {
            try {
                val response = ApiClient.apiService.getDrinks("Bearer $LOGIN_TOKEN")

                if (response.isSuccessful && response.body() != null) {
                    val content = response.body()

                    if (content != null) {
                        view.adapter = MyDrinksRecyclerViewAdapter(content, mContext)
                    }
                }
            } catch (e: Exception) {

            }
        }
    }

    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(mContext: Context) = DrinksFragment(mContext)
    }

    override val coroutineContext: CoroutineContext = Job()
}