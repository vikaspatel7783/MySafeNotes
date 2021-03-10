package com.vikas.mobile.mysafenotes.ui.dashboard

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.MainThread
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import androidx.viewpager.widget.ViewPager
import androidx.appcompat.app.AppCompatActivity
import com.vikas.mobile.mysafenotes.R
import com.vikas.mobile.mysafenotes.data.entity.Category
import com.vikas.mobile.mysafenotes.data.entity.Note
import com.vikas.mobile.mysafenotes.ui.AddCategoryDialogFragment
import com.vikas.mobile.mysafenotes.ui.AddNoteActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DashboardActivity : AppCompatActivity() {

    private val dashboardViewModel: DashboardViewModel by viewModels()
    private lateinit var viewPager: ViewPager
    private var categoryMap = emptyMap<String, Long>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        viewPager = findViewById(R.id.view_pager)
        dashboardViewModel.getAllCategories().observe(this, { categoryList ->
            categoryMap = categoryList.map { category ->
                category.name to category.id
            }.toMap()
            setPagerView(categoryList)
        })

        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { view ->
            AddCategoryDialogFragment.newInstance().show(supportFragmentManager, "TagAddCategory")
        }

        findViewById<FloatingActionButton>(R.id.fab_add_note).setOnClickListener {
            resultLauncher.launch(Intent(applicationContext, AddNoteActivity::class.java).apply {
                putExtra(AddNoteActivity.KEY_CATEGORY_NAME, viewPager.adapter?.getPageTitle(viewPager.currentItem))
            })
        }
    }

    private var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val data: Intent? = result.data

            val categoryId = categoryMap[viewPager.adapter?.getPageTitle(viewPager.currentItem)]!!
            val noteContent = data?.extras?.get(AddNoteActivity.KEY_NOTE_CONTENT).toString()

            Note(
                categoryId = categoryId,
                noteContent = noteContent
            ).let { dashboardViewModel.addNewNote(it) }

            Toast.makeText(applicationContext,
                "Note added", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setPagerView(allCategories: List<Category>) {
        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager, allCategories)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        tabs.setupWithViewPager(viewPager)
    }

}