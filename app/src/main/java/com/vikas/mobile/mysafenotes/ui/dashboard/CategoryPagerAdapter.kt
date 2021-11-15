package com.vikas.mobile.mysafenotes.ui.dashboard

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.vikas.mobile.mysafenotes.data.entity.Category
import java.util.*

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
class CategoryPagerAdapter(
        manager: FragmentManager,
        private var allCategories: List<Category>
) :
    FragmentPagerAdapter(manager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {
        return CategoryTabFragment.newInstance(allCategories[position].id)
    }

    override fun getPageTitle(position: Int): CharSequence {
        return allCategories[position].name.content.toUpperCase(Locale.ROOT)
    }

    fun getCurrentCategory(position: Int): Category {
        return allCategories[position]
    }

    override fun getCount(): Int {
        return allCategories.size
    }

    fun setData(allCategories: List<Category>) {
        this.allCategories = allCategories
    }

}