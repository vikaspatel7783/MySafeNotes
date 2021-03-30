package com.vikas.mobile.mysafenotes.ui.dashboard

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.vikas.mobile.mysafenotes.R
import com.vikas.mobile.mysafenotes.authandcrypto.AppAuthCallback
import com.vikas.mobile.mysafenotes.authandcrypto.BiometricPromptUtils
import com.vikas.mobile.mysafenotes.data.entity.Category
import com.vikas.mobile.mysafenotes.data.entity.Note
import com.vikas.mobile.mysafenotes.ui.AddCategoryDialogFragment
import com.vikas.mobile.mysafenotes.ui.AddUpdateNoteActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DashboardActivity : AppCompatActivity() {

    private val dashboardViewModel: DashboardViewModel by viewModels()
    private lateinit var viewPager: ViewPager
    private lateinit var fabAddNoteButton: FloatingActionButton
    private var categoryMap = emptyMap<Long, String>()
    private var categoryList = emptyList<Category>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        BiometricPromptUtils.showUserAuthentication(this,
        object : AppAuthCallback {
            override fun onAuthenticationSucceeded() {
                doPostUserAuthentication()
            }

            override fun authenticationsNotPresent() {
                //TODO("To be better handled")
                Toast.makeText(this@DashboardActivity, "Authentications not provided on this device", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun doPostUserAuthentication() {
        fabAddNoteButton = findViewById(R.id.fab_add_note)
        viewPager = findViewById(R.id.view_pager)
        viewPager.adapter = CategoryPagerAdapter(this, supportFragmentManager, categoryList)
        val tabs: TabLayout = findViewById(R.id.tabs)
        tabs.setupWithViewPager(viewPager)

        dashboardViewModel.getAllCategories().observe(this, { receivedCategories ->
            categoryMap = receivedCategories.map { category ->
                category.id to category.name.content
            }.toMap()

            categoryList = receivedCategories
            fabAddNoteButton.isEnabled = categoryList.isNotEmpty()

            //TODO: notifyDatasetChange not working. Should not create new instance everytime when data updates
            viewPager.adapter = CategoryPagerAdapter(this, supportFragmentManager, categoryList)
            //(viewPager.adapter as CategoryPagerAdapter).notifyDataSetChanged()
        })

        findViewById<FloatingActionButton>(R.id.fab_add_category).setOnClickListener {
            AddCategoryDialogFragment.newInstance().show(supportFragmentManager, "TagAddCategory")
        }

        findViewById<FloatingActionButton>(R.id.fab_add_note).setOnClickListener {
            showAddUpdateNoteActivity()
        }
    }
    private fun showAddUpdateNoteActivity() {
        startActivity(prepareIntentForAddUpdateNewNote(getCurrentCategory()))
    }

    fun onNoteClicked(note: Note) {
        prepareIntentForAddUpdateNewNote(getCurrentCategory()).apply {
            putExtra(AddUpdateNoteActivity.KEY_NOTE_ID, note.id)
        }.run {
            startActivity(this)
        }
    }

    private fun getCurrentCategory() = ((viewPager.adapter) as CategoryPagerAdapter).getCurrentCategory(viewPager.currentItem)

    private fun prepareIntentForAddUpdateNewNote(currentCategory: Category): Intent {
        return Intent(applicationContext, AddUpdateNoteActivity::class.java).apply {
            putExtra(AddUpdateNoteActivity.KEY_CATEGORY_ID, currentCategory.id)
            putExtra(AddUpdateNoteActivity.KEY_CATEGORY_NAME, currentCategory.name.content)
        }
    }

}