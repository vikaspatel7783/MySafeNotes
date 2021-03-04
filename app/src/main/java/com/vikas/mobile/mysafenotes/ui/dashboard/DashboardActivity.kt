package com.vikas.mobile.mysafenotes.ui.dashboard

import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import androidx.viewpager.widget.ViewPager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.vikas.mobile.mysafenotes.InstanceFactory
import com.vikas.mobile.mysafenotes.R
import com.vikas.mobile.mysafenotes.data.entity.Category

class DashboardActivity : AppCompatActivity() {

    lateinit var dashboardViewModel: DashboardViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        dashboardViewModel = ViewModelProvider(this, DashboardViewModelFactory(InstanceFactory.repository)).get(DashboardViewModel::class.java)
        dashboardViewModel.getAllCategories().observe(this, {
            setPagerView(it)
        })

        val fab: FloatingActionButton = findViewById(R.id.fab)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
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