package instrumentedTests.ui

import android.content.Context
import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.gabr.gabc.kelo.R
import com.gabr.gabc.kelo.constants.Constants
import com.gabr.gabc.kelo.firebase.ChoreQueries
import com.gabr.gabc.kelo.firebase.GroupQueries
import com.gabr.gabc.kelo.firebase.UserQueries
import com.gabr.gabc.kelo.main.MainActivity
import com.gabr.gabc.kelo.models.Group
import com.gabr.gabc.kelo.models.User
import com.google.firebase.FirebaseApp
import instrumentedTests.ui.utils.DisableAnimationsRule
import instrumentedTests.ui.utils.keepScreenActive
import kotlinx.coroutines.runBlocking
import org.junit.*
import org.junit.runner.RunWith

/** Defines the Settings UI Tests */
@RunWith(AndroidJUnit4::class)
class SettingsTest {
    private val intent = Intent(ApplicationProvider.getApplicationContext(), MainActivity::class.java)

    @Rule(order = 0)
    @JvmField
    val disabledAnimations = DisableAnimationsRule()

    @Rule(order = 1)
    @JvmField
    val activityScenario = ActivityScenarioRule<MainActivity>(intent)

    companion object {
        private val group = Group("UI_GROUP", "generic group", "EUR")
        private val user = User("UI_USER", "Gabriel", 0, true)

        /** Initializes and creates Firebase needed data for the tests */
        @JvmStatic
        @BeforeClass
        fun setUpFirebase() {
            val context = InstrumentationRegistry.getInstrumentation().targetContext
            FirebaseApp.initializeApp(context)
            runBlocking {
                GroupQueries().createGroup(group)
                UserQueries().createUser(user, group.id)
            }

            // Sets shared preferences for activities to use
            val gr = context.getSharedPreferences(Constants.GROUP_ID, Context.MODE_PRIVATE)
            with (gr.edit()) {
                putString(Constants.GROUP_ID, group.id)
                commit()
            }

            val us = context.getSharedPreferences(Constants.USER_ID, Context.MODE_PRIVATE)
            with (us.edit()) {
                putString(Constants.USER_ID, user.id)
                commit()
            }
        }

        /** Cleans up Firebase */
        @JvmStatic
        @AfterClass
        fun cleanFirebase() {
            runBlocking {
                UserQueries().deleteAllUsers(group.id)
                ChoreQueries().deleteAllChores(group.id)
                GroupQueries().deleteGroup(group.id)
            }
        }
    }

    /** Function called before each test to go to the desired activity */
    @Before
    fun pressSettingsTab() {
        activityScenario.scenario.onActivity { it.keepScreenActive() }
        onView(withId(R.id.settings_menu)).perform(click())
    }

    /**
     * Test that verifies that leaving a group automatically removes the user and sets the current
     * user to the Welcome Page
     * */
    @Test
    fun dialogOnLeaveGroupAppearsAndRedirectsToWelcomePage() {
        onView(withId(R.id.settingsExitGroupButton)).perform(scrollTo()).perform(click())
        onView(withText(R.string.settings_dialog_msg_leave_group)).inRoot(isDialog()).check(matches(isDisplayed()))
        onView(withId(android.R.id.button1)).perform(click())
        Thread.sleep(1000)
        onView(withText(R.string.welcome_to_kelo)).check(matches(isDisplayed()))
    }

    /**
     * Test that verifies that removing a group automatically removes the user and sets the current
     * user to the Welcome Page
     * */
    @Test
    fun dialogOnDeleteGroupAppearsAndRedirectsToWelcomePage() {
        onView(withId(R.id.settingsRemoveGroupButton)).perform(scrollTo()).perform(click())
        onView(withText(R.string.settings_dialog_msg_delete_group)).inRoot(isDialog()).check(matches(isDisplayed()))
        onView(withId(android.R.id.button1)).perform(click())
        Thread.sleep(1000)
        onView(withText(R.string.welcome_to_kelo)).check(matches(isDisplayed()))
    }

    /** Test that verifies that the currency dialog appears upon selection if admin */
    @Test
    fun currencyChangeDialogAppearsOnAdmin() {
        onView(withId(R.id.settingsCurrencyButton)).perform(scrollTo()).perform(click())
        onView(withId(R.id.currencyList)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(1, click()))
        onView(withId(R.id.settingsCurrencyButton)).check(matches(withText("ARS")))
    }
}