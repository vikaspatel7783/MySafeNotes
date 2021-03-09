package com.vikas.mobile.mysafenotes.ui

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.vikas.mobile.mysafenotes.R
import com.vikas.mobile.mysafenotes.ui.dashboard.DashboardActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AddCategoryTest {

//    val context = InstrumentationRegistry.getInstrumentation().targetContext

    @get:Rule
    var activityRule: ActivityScenarioRule<DashboardActivity>
            = ActivityScenarioRule(DashboardActivity::class.java)

    @Test
    fun testAddCategoryScreenDisplayed() {
        // Given - add category button is displayed
        onView(withId(R.id.fab)).perform(click())

        // When - category name is entered and add save button clicked
        onView(withId(R.id.edtTextAddCategory)).perform(typeText("PERSONAL"), closeSoftKeyboard())
        onView(withId(R.id.buttonAddCategoryAdd)).perform(click())

        // Then - category should be displayed on dashboard screen

    }

}