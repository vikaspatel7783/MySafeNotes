package com.vikas.mobile.mysafenotes.ui.dashboard

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import androidx.viewpager.widget.ViewPager
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
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
    private var categoryMap = emptyMap<Long, String>()
    private var categoryList = emptyList<Category>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        viewPager = findViewById(R.id.view_pager)
        viewPager.adapter = CategoryPagerAdapter(this, supportFragmentManager, categoryList)
        val tabs: TabLayout = findViewById(R.id.tabs)
        tabs.setupWithViewPager(viewPager)

        dashboardViewModel.getAllCategories().observe(this, { receivedCategories ->
            categoryMap = receivedCategories.map { category ->
                category.id to category.name
            }.toMap()

            categoryList = receivedCategories
            //TODO: notifyDatasetChange not working. Should not create new instance everytime when data updates
            viewPager.adapter = CategoryPagerAdapter(this, supportFragmentManager, categoryList)
            //(viewPager.adapter as CategoryPagerAdapter).notifyDataSetChanged()
        })

        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener {
            AddCategoryDialogFragment.newInstance().show(supportFragmentManager, "TagAddCategory")
        }

        findViewById<FloatingActionButton>(R.id.fab_add_note).setOnClickListener {
            resultLauncher.launch(Intent(applicationContext, AddNoteActivity::class.java).apply {
                putExtra(AddNoteActivity.KEY_CATEGORY_NAME,
                        ((viewPager.adapter) as CategoryPagerAdapter).getCurrentCategory(viewPager.currentItem).name)
            })
        }
    }

    private var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val data: Intent? = result.data

            val currentCategory =  ((viewPager.adapter) as CategoryPagerAdapter).getCurrentCategory(viewPager.currentItem)
            val noteContent = data?.extras?.get(AddNoteActivity.KEY_NOTE_CONTENT).toString()

            Note(
                categoryId = currentCategory.id,
                noteContent = noteContent
            ).let { dashboardViewModel.addNewNote(it) }

            Snackbar.make(viewPager, "NOTE ADDED", Snackbar.LENGTH_SHORT)
                    //.setAction("Action", null)
                    .show()
        }
    }

}