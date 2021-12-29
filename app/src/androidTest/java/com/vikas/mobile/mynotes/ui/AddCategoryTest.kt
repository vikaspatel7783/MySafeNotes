package com.vikas.mobile.mynotes.ui

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.vikas.mobile.mynotes.R
import com.vikas.mobile.mynotes.data.MySafeNotesDatabase
import com.vikas.mobile.mynotes.ui.dashboard.DashboardActivity
import org.junit.After
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AddCategoryTest {

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
    fun testAddCategoryScreenDisplayed() {
        Espresso.openActionBarOverflowOrOptionsMenu(ApplicationProvider.getApplicationContext())

        // Given - add category button is displayed
        onView(withText(context.getString(R.string.appbar_add_category))).perform(click())

        // When - category name is entered and add save button clicked
        onView(withId(R.id.edtTextAddCategory)).perform(typeText("PERSONAL"), closeSoftKeyboard())
        onView(withId(R.id.buttonAddCategoryAdd)).perform(click())

        // Then - category should be displayed on dashboard screen
        onView(withText("PERSONAL")).check(ViewAssertions.matches(isDisplayed()))
    }

}