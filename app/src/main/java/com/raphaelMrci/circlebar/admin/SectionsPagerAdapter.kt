package com.raphaelMrci.circlebar.admin

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.raphaelMrci.circlebar.R
import com.raphaelMrci.circlebar.admin.cocktails.CocktailsFragment
import com.raphaelMrci.circlebar.admin.drinks.DrinksFragment
import com.raphaelMrci.circlebar.admin.slots.SlotsFragment

private val TAB_TITLES = arrayOf(
    R.string.tab_text_1,
    R.string.tab_text_2,
    R.string.tab_text_3
)

class SectionsPagerAdapter(private val context: Context, fm: FragmentManager, private val fab: FloatingActionButton) :
    FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> DrinksFragment.newInstance(context, fab)
            1 -> CocktailsFragment.newInstance(context, fab)
            2 -> SlotsFragment.newInstance(context, fab)
            else -> PlaceholderFragment.newInstance(position + 1)
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return context.resources.getString(TAB_TITLES[position])
    }

    override fun getCount(): Int {
        // Show 3 total pages.
        return 3
    }
}