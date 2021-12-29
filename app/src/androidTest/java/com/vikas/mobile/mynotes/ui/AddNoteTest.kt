package com.vikas.mobile.mynotes.ui

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
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
    fun testNewNote_CRUD_operation() {

        val categoryName = "banking"
        val noteContent = "MyBank\nAccount: 12345678\nPassword: 00000abcdAA*\nType: Loan"
        val noteContentUpdated = "MyBank\nAccount: 987654321\nPassword: 111***000BBB\nType: Savings"

        // add category
        openActionBarOverflowOrOptionsMenu(ApplicationProvider.getApplicationContext())
        onView(withText(context.getString(R.string.appbar_add_category))).perform(click())
        onView(withId(R.id.edtTextAddCategory)).perform(typeText(categoryName), closeSoftKeyboard())
        onView(withId(R.id.buttonAddCategoryAdd)).perform(click())

        // add note under category
        openActionBarOverflowOrOptionsMenu(ApplicationProvider.getApplicationContext())
        onView(withText(context.getString(R.string.appbar_add_note))).perform(click())
        onView(withId(R.id.edtTextNote)).perform(typeText(noteContent))
        onView(withId(R.id.buttonAddUpdateNote)).perform(click())

        // onView(withId(R.id.note_content_item)).perform().check(matches(withText("This are my personal notes")))
        onView(withText(getHeaderText(noteContent))).check(matches(isDisplayed()))

        // update note content
        onView(withText(getContentText(noteContent))).perform(click())
        onView(withId(R.id.edtTextNote)).perform(clearText(), typeText(noteContentUpdated))
        onView(withId(R.id.buttonAddUpdateNote)).perform(click())

        // verify note content is updated
        onView(withText(getContentText(noteContentUpdated))).check(matches(isDisplayed()))

        // remove the note
        onView(withId(R.id.note_delete)).perform(click())

        // check snake-bar and perform 'yes' click
        onView(withId(com.google.android.material.R.id.snackbar_text)).check(matches(isDisplayed()))
        onView(withId(com.google.android.material.R.id.snackbar_action)).perform(click())

        // verify note content is not shown (removed) over the dashboard
        onView(withText(getHeaderText(noteContentUpdated))).check(doesNotExist())
    }

    private fun getHeaderText(noteContent: String): String {
        val defaultHeaderCharsCountLength = 35
        return noteContent.substring(startIndex = 0,
            endIndex = if (noteContent.indexOf("\n") == -1) if(noteContent.length < defaultHeaderCharsCountLength) noteContent.length else defaultHeaderCharsCountLength
            else noteContent.indexOf("\n"))
    }

    private fun getContentText(noteContent: String): String {
        return noteContent.substring(startIndex = if (noteContent.indexOf("\n") == -1) 0 else noteContent.indexOf("\n")+1,
            endIndex = noteContent.length)
    }
}