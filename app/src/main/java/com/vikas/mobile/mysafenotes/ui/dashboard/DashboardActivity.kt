package com.vikas.mobile.mysafenotes.ui.dashboard

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.vikas.mobile.authandcrypto.AppAuthCallback
import com.vikas.mobile.authandcrypto.BiometricPromptUtils
import com.vikas.mobile.mysafenotes.R
import com.vikas.mobile.mysafenotes.data.entity.Category
import com.vikas.mobile.mysafenotes.data.entity.Note
import com.vikas.mobile.mysafenotes.ui.AddCategoryDialogFragment
import com.vikas.mobile.mysafenotes.ui.AddUpdateNoteActivity
import com.vikas.mobile.mysafenotes.ui.SearchNoteDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class DashboardActivity : AppCompatActivity() {

    private val dashboardViewModel: DashboardViewModel by viewModels()
    private lateinit var viewPager: ViewPager
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

            override fun onUserCancels() {
                finish()
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.app_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_search_note -> {
                SearchNoteDialogFragment.newInstance().show(supportFragmentManager, "TagSearchNote")
                true
            }
            R.id.menu_add_category -> {
                AddCategoryDialogFragment.newInstance().show(supportFragmentManager, "TagAddCategory")
                true
            }
            R.id.menu_add_note -> {
                showAddUpdateNoteActivity()
                true
            }
            R.id.menu_delete_category -> {
                deleteCategoryAndNotes()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun deleteCategoryAndNotes() {
        Snackbar.make(viewPager, String.format(getString(R.string.prompt_confirm_delete_category, getCurrentCategory().name.content.toUpperCase(
            Locale.ROOT))), 4000)
            .setAction("YES") { _ ->
                dashboardViewModel.deleteNotesForCategory(getCurrentCategory().id)
            }.show()
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        super.onPrepareOptionsMenu(menu)
        menu?.findItem(R.id.menu_add_note)?.isEnabled = categoryList.isNotEmpty()
        menu?.findItem(R.id.menu_delete_category)?.isEnabled = categoryList.isNotEmpty()
        return true
    }

    private fun doPostUserAuthentication() {
        viewPager = findViewById(R.id.view_pager)
        viewPager.offscreenPageLimit = 3
        viewPager.adapter = CategoryPagerStateAdapter(supportFragmentManager, categoryList)
        val tabs: TabLayout = findViewById(R.id.tabs)
        tabs.setupWithViewPager(viewPager)

        dashboardViewModel.getAllCategories().observe(this, { receivedCategories ->
            categoryMap = receivedCategories.map { category ->
                category.id to category.name.content
            }.toMap()

            categoryList = receivedCategories
            //FIXME: as notifyDataSet doesn't work, below is interim patch to redraw entire list
            viewPager.adapter = CategoryPagerStateAdapter(supportFragmentManager, categoryList)
            val tabs: TabLayout = findViewById(R.id.tabs)
            tabs.setupWithViewPager(viewPager)

            // show no notes layout if it is empty
            if (categoryList.isNullOrEmpty()) {
                findViewById<ViewGroup>(R.id.no_notes_layout).visibility = View.VISIBLE
            } else {
                findViewById<ViewGroup>(R.id.no_notes_layout).visibility = View.GONE
            }
        })
    }

    private fun showAddUpdateNoteActivity() {
        getCurrentCategory().let { category ->
            AddUpdateNoteActivity.createIntent(
                    applicationContext,
                    categoryId = category.id,
                    categoryName = category.name.content
            ).let {
                startActivity(it)
            }
        }
    }

    fun onNoteClicked(note: Note) {
        AddUpdateNoteActivity.createIntent(
                applicationContext,
                categoryId = note.categoryId,
                categoryName = getCurrentCategory().name.content,
                noteId = note.id)
        .let {
            startActivity(it)
        }
    }

    private fun getCurrentCategory() = ((viewPager.adapter) as CategoryPagerStateAdapter).getCurrentCategory(viewPager.currentItem)

}