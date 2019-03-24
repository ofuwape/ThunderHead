package com.thunderhead.searchresults

import android.content.Intent
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isFocusable
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.thunderhead.searchresults.core.SearchResults
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class SearchResultsTest {

    @Rule
    @JvmField
    var activityRule = ActivityTestRule<MockActivity>(MockActivity::class.java, true, false)

    @Before
    fun setup() {
        MockActivity.layout = R.layout.mock_activity_layout
    }


    private fun restartActivity() {
        if (activityRule.activity != null) {
            activityRule.finishActivity()
        }
        activityRule.launchActivity(Intent())
    }

    @Test
    fun testViewIsDisplayed() {
        restartActivity()
        Espresso.onView(withId(R.id.search_recycler_view)).check(matches(isFocusable()))
        activityRule.activity.findViewById<SearchResults>(R.layout.search_results_view)
    }


}
