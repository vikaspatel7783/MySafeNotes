package com.vikas.mobile.mysafenotes.ui.dashboard

import android.os.Bundle
import androidx.activity.viewModels
import androidx.annotation.MainThread
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import androidx.viewpager.widget.ViewPager
import androidx.appcompat.app.AppCompatActivity
import com.vikas.mobile.mysafenotes.R
import com.vikas.mobile.mysafenotes.data.entity.Category
import com.vikas.mobile.mysafenotes.ui.AddCategoryDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DashboardActivity : AppCompatActivity() {

    private val dashboardViewModel: DashboardViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        dashboardViewModel.getAllCategories().observe(this, {
            setPagerView(it)
        })

        val fab: FloatingActionButton = findViewById(R.id.fab)

        fab.setOnClickListener { view ->
            AddCategoryDialogFragment.newInstance().show(supportFragmentManager, "TagAddCategory")
        }
    }

    private fun setPagerView(allCategories: List<Category>) {
        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager, allCategories)
        val viewPager: ViewPager = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        tabs.setupWithViewPager(viewPager)
    }

}