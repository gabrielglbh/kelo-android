package instrumentedTests.ui

/*import android.content.Context
import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.platform.app.InstrumentationRegistry
import com.gabr.gabc.kelo.R
import com.gabr.gabc.kelo.constants.Constants
import com.gabr.gabc.kelo.dataModels.Group
import com.gabr.gabc.kelo.dataModels.User
import com.gabr.gabc.kelo.firebase.ChoreQueries
import com.gabr.gabc.kelo.firebase.GroupQueries
import com.gabr.gabc.kelo.firebase.UserQueries
import com.gabr.gabc.kelo.main.MainActivity
import com.gabr.gabc.kelo.utils.DatesSingleton
import com.google.firebase.FirebaseApp
import instrumentedTests.ui.utils.DisableAnimationsRule
import instrumentedTests.ui.utils.keepScreenActive
import kotlinx.coroutines.runBlocking
import org.junit.*
import java.util.*

/** Defines the Add Reward UI Test */
class AddRewardTest {
    private val intent = Intent(ApplicationProvider.getApplicationContext(), MainActivity::class.java)

    @Rule(order = 0)
    @JvmField
    val disabledAnimations = DisableAnimationsRule()

    @Rule(order = 1)
    @JvmField
    val activityScenario = ActivityScenarioRule<MainActivity>(intent)

    companion object {
        private const val expectedDescription = "Gabo invita a cervezas"
        private val group = Group("UI_GROUP", "generic group", "EUR")
        private val user = User("UI_USER", "Gabriel", 0, isAdmin = true)

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
    fun pressFAB() {
        activityScenario.scenario.onActivity { it.keepScreenActive() }
        onView(withId(R.id.settings_menu)).perform(click())
    }

    /** Tests the full creation of the reward of the group */
    @Test
    fun verifyDataOfRewardIsCorrectlyDisplayedInSettingsUponCreation() {
        val nextWeek = Calendar.getInstance()
        nextWeek.add(Calendar.WEEK_OF_YEAR, 1)
        val parsedNextWeek = "Every Week (${DatesSingleton.parseCalendarToStringOnList(nextWeek)})"

        Thread.sleep(2000)
        onView(withId(R.id.rewardParent)).perform(scrollTo()).perform(click())

        onView(withId(R.id.rewardDetailNameEditText)).perform(typeText(expectedDescription))
        closeSoftKeyboard()

        onView(withId(R.id.rewardDetailPeriodicityButton)).perform(click())
        onView(withId(R.id.periodicityRecyclerView)).perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(1, click()))

        onView(withId(R.id.toolbar_done)).perform(click())

        Thread.sleep(2000)
        // Verify reward description and frequency
        onView(withId(R.id.rewardParent)).perform(scrollTo()).check(matches(hasDescendant(withText(expectedDescription))))
        onView(withId(R.id.rewardParent)).perform(scrollTo()).check(matches(hasDescendant(withText(parsedNextWeek))))
    }
}*/