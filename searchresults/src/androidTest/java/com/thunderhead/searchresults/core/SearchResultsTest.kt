package com.thunderhead.searchresults.core

import android.content.Context
import android.content.Intent
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isFocusable
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.thunderhead.searchresults.MockActivity
import com.thunderhead.searchresults.MockWebServerUtil
import com.thunderhead.searchresults.R
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.lang.ref.WeakReference


@RunWith(AndroidJUnit4::class)
class SearchResultsTest {


    @Rule
    @JvmField
    var activityRule = ActivityTestRule<MockActivity>(MockActivity::class.java, true, false)


    private var mockWebServer: MockWebServer? = null

    @Before
    fun setup() {

        val context: Context = InstrumentationRegistry.getInstrumentation().context
        MockActivity.layout = R.layout.mock_activity_layout
        mockWebServer = MockWebServerUtil.startMockServer(context,
                MockWebServerUtil.MockWebServerType.GOOD)
    }

    @After
    fun tearDown() {
        mockWebServer?.shutdown()
    }

    private fun getMyActivity(): MockActivity? {
        if (activityRule.activity is MockActivity) {
            return activityRule.activity
        }
        return (activityRule.activity as WeakReference<MockActivity>?)?.get()
    }


    private fun restartActivity() {
        if (getMyActivity() != null) {
            activityRule.finishActivity()
        }
        activityRule.launchActivity(Intent())
    }

    @Test
    fun testViewIsDisplayed() {
        restartActivity()
        Espresso.onView(withId(R.id.search_recycler_view)).check(matches(isFocusable()))
    }


}
