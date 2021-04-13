package com.vikas.mobile.mysafenotes.ui.dashboard

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.vikas.mobile.authandcrypto.AppAuthCallback
import com.vikas.mobile.authandcrypto.BiometricPromptUtils
import com.vikas.mobile.mysafenotes.R
import com.vikas.mobile.mysafenotes.data.entity.Category
import com.vikas.mobile.mysafenotes.data.entity.Note
import com.vikas.mobile.mysafenotes.ui.AddCategoryDialogFragment
import com.vikas.mobile.mysafenotes.ui.AddUpdateNoteActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DashboardActivity : AppCompatActivity() {

    private val dashboardViewModel: DashboardViewModel by viewModels()
    private lateinit var viewPager: ViewPager
    private lateinit var fabAddNoteButton: TextView
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
                AlertDialog.Builder(this@DashboardActivity)
                        .setTitle(getString(R.string.error_no_authentication_title))
                        .setMessage(getString(R.string.error_no_authentication_method))
                        .setCancelable(false)
                        .setPositiveButton(android.R.string.ok) { _: DialogInterface, _: Int ->
                            finish()
                        }
                        .show()
            }
        })
    }

    private fun doPostUserAuthentication() {
        fabAddNoteButton = findViewById(R.id.fab_add_note)
        viewPager = findViewById(R.id.view_pager)
        viewPager.adapter = CategoryPagerAdapter(supportFragmentManager, categoryList)
        val tabs: TabLayout = findViewById(R.id.tabs)
        viewPager.adapter = CategoryPagerAdapter(supportFragmentManager, categoryList)
        tabs.setupWithViewPager(viewPager)

        dashboardViewModel.getAllCategories().observe(this, { receivedCategories ->
            categoryMap = receivedCategories.map { category ->
                category.id to category.name.content
            }.toMap()

            categoryList = receivedCategories
            fabAddNoteButton.visibility = if (categoryList.isEmpty()) View.INVISIBLE else View.VISIBLE

            (viewPager.adapter as CategoryPagerAdapter).setData(categoryList)
            (viewPager.adapter as CategoryPagerAdapter).notifyDataSetChanged()
        })

        findViewById<TextView>(R.id.fab_add_category).setOnClickListener {
            AddCategoryDialogFragment.newInstance().show(supportFragmentManager, "TagAddCategory")
        }

        findViewById<TextView>(R.id.fab_add_note).setOnClickListener {
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