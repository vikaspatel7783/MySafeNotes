package com.vikas.mobile.mynotes.ui.dashboard

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.menu.MenuBuilder
import androidx.viewpager.widget.ViewPager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.tabs.TabLayout
import com.vikas.mobile.authandcrypto.AppAuthCallback
import com.vikas.mobile.authandcrypto.BiometricPromptUtils
import com.vikas.mobile.mynotes.R
import com.vikas.mobile.mynotes.data.entity.Category
import com.vikas.mobile.mynotes.data.entity.Note
import com.vikas.mobile.mynotes.data.entity.Setting
import com.vikas.mobile.mynotes.ui.AddCategoryDialogFragment
import com.vikas.mobile.mynotes.ui.AddUpdateNoteActivity
import com.vikas.mobile.mynotes.ui.SearchNoteDialogFragment
import com.vikas.mobile.mynotes.ui.SettingsActivity
import dagger.hilt.android.AndroidEntryPoint
import observeOnce
import java.util.*

@AndroidEntryPoint
class DashboardActivity : AppCompatActivity() {

    private val dashboardViewModel: DashboardViewModel by viewModels()
    private lateinit var viewPager: ViewPager
    private var categoryMap = emptyMap<Long, String>()
    private var categoryList = emptyList<Category>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE)
        setContentView(R.layout.activity_dashboard)

        dashboardViewModel.getAuthSetting().observeOnce(this, { authSetting ->
            Setting.interpretAuthSettingValue(authSetting.value).let { authEnabled ->
                if (authEnabled) showAuthentication() else doPostUserAuthentication()
            }
        })
    }

    private fun showAuthentication() {
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
        if (menu is MenuBuilder) (menu as MenuBuilder).setOptionalIconsVisible(true)
        menuInflater.inflate(R.menu.app_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_settings -> {
                startActivity(Intent(this, SettingsActivity::class.java))
                true
            }
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
        val dialog = BottomSheetDialog(this)
        val dialogView = layoutInflater.inflate(R.layout.confirm_note_category_deletion_layout, null)
        dialog.setContentView(dialogView)

        dialogView.findViewById<TextView>(R.id.labelConfirmDeletion).text =
                String.format(getString(R.string.prompt_confirm_delete_category,
                        getCurrentCategory().name.content.toUpperCase(Locale.ROOT)))
        dialogView.findViewById<Button>(R.id.buttonYes).setOnClickListener {
            dashboardViewModel.deleteNotesForCategory(getCurrentCategory().id)
            dialog.dismiss()
        }
        dialogView.findViewById<Button>(R.id.buttonCancel).setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        super.onPrepareOptionsMenu(menu)
        menu?.findItem(R.id.menu_add_note)?.isVisible = categoryList.isNotEmpty()
        menu?.findItem(R.id.menu_search_note)?.isVisible = categoryList.isNotEmpty()
        menu?.findItem(R.id.menu_delete_category)?.isVisible = categoryList.isNotEmpty()
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
                tabs.visibility = View.GONE
            } else {
                findViewById<ViewGroup>(R.id.no_notes_layout).visibility = View.GONE
                tabs.visibility = View.VISIBLE
            }
            invalidateOptionsMenu()
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