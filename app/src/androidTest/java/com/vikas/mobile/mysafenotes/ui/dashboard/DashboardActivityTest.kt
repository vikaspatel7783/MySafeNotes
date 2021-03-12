package com.vikas.mobile.mysafenotes.ui.dashboard

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.swipeLeft
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.vikas.mobile.mysafenotes.R
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DashboardActivityTest {

    @get:Rule
    var activityRule: ActivityScenarioRule<DashboardActivity>
            = ActivityScenarioRule(DashboardActivity::class.java)

    @Test
    fun testFabButtonClick() {
        onView(withId(R.id.fab_add_category)).perform(click())
    }

    @Test
    fun testTabs() {
        onView(withId(R.id.view_pager)).perform(swipeLeft());
    }
}