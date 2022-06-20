package com.raphaelMrci.circlebar.admin.slots

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.raphaelMrci.circlebar.LOGIN_TOKEN
import com.raphaelMrci.circlebar.R
import com.raphaelMrci.circlebar.models.Drink
import com.raphaelMrci.circlebar.models.Slot
import com.raphaelMrci.circlebar.network.ApiClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class SlotsFragment(private val mContext: Context, private val fab: FloatingActionButton) : Fragment(), CoroutineScope {
    @Deprecated("Deprecated in Java")
    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)

        if (isVisibleToUser) {
            fab.hide()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // getSlots()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_slots_list, container, false)
        if (view is RecyclerView) {
            with(view) {
                layoutManager = LinearLayoutManager(mContext)
                getSlots(view)
            }
        }
        return view
    }


    private fun getSlots(recv: RecyclerView) {
        launch(Dispatchers.Main) {
            try {
                val response = ApiClient.apiService.getSlots("Bearer $LOGIN_TOKEN")

                if (response.isSuccessful) {
                    launch(Dispatchers.Main) {
                        val response1 = ApiClient.apiService.getDrinks("Bearer $LOGIN_TOKEN")

                        if (response1.isSuccessful) {
                            recv.adapter = response.body()?.let { slots ->
                                slots.sortBy { slot -> slot.id }
                                response1.body()?.let { it1 -> MySlotsRecyclerViewAdapter(slots, it1, mContext) }
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                // TODO: Toast
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(mContext: Context, fab: FloatingActionButton) = SlotsFragment(mContext, fab)
    }

    override val coroutineContext: CoroutineContext = Job()
}