package com.vikas.mobile.mysafenotes.ui

import android.content.Context
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.vikas.mobile.mysafenotes.R
import com.vikas.mobile.mysafenotes.data.MySafeNotesDatabase
import com.vikas.mobile.mysafenotes.ui.dashboard.DashboardActivity
import org.junit.After
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AddNoteTest {

    private val context: Context = InstrumentationRegistry.getInstrumentation().targetContext
    private val db = MySafeNotesDatabase.getInstance(context).openHelper.writableDatabase

    @get:Rule
    var activityRule: ActivityScenarioRule<DashboardActivity>
            = ActivityScenarioRule(DashboardActivity::class.java)

    @After
    fun purgeDatabase() {
        db.execSQL("DELETE FROM note_table")
        db.execSQL("DELETE FROM category_table")
    }

    @Test
    fun testNewNoteIsAddedSuccessfully() {
        // Given - add category button is displayed
        onView(withId(R.id.fab_add_category)).perform(click())

        // When - category name is entered and add save button clicked
        onView(withId(R.id.edtTextAddCategory)).perform(typeText("PERSONAL"), closeSoftKeyboard())
        onView(withId(R.id.buttonAddCategoryAdd)).perform(click())
        // When - note details is entered and saved
        onView(withId(R.id.fab_add_note)).perform(click())
        onView(withId(R.id.edtTextNote)).perform(typeText("This are my personal notes"))
        onView(withId(R.id.buttonAddUpdateNote)).perform(click())

        // Then - category should be displayed on dashboard screen
//        onView(withId(R.id.note_content_item)).perform().check(matches(withText("This are my personal notes")))
        onView(withText("This are my personal notes")).check(matches(isDisplayed()))
    }

}