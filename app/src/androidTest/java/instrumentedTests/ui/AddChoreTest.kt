package instrumentedTests.ui

import android.content.Context
import android.content.Intent
import android.widget.DatePicker
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.PickerActions
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.gabr.gabc.kelo.MainActivity
import com.gabr.gabc.kelo.R
import com.gabr.gabc.kelo.constants.GROUP_ID
import com.gabr.gabc.kelo.constants.USER_ID
import com.gabr.gabc.kelo.firebase.ChoreQueries
import com.gabr.gabc.kelo.firebase.GroupQueries
import com.gabr.gabc.kelo.firebase.UserQueries
import com.gabr.gabc.kelo.models.Group
import com.gabr.gabc.kelo.models.User
import com.gabr.gabc.kelo.utils.UtilsSingleton
import com.google.firebase.FirebaseApp
import instrumentedTests.ui.utils.ColorViewMatcher.Companion.matchesBackgroundColor
import instrumentedTests.ui.utils.DisableAnimationsRule
import instrumentedTests.ui.utils.RecyclerViewMatcher.Companion.atPosition
import instrumentedTests.ui.utils.keepScreenActive
import kotlinx.coroutines.runBlocking
import org.hamcrest.Matchers.equalTo
import org.junit.*
import org.junit.runner.RunWith
import java.util.*
import kotlin.reflect.jvm.jvmName

@RunWith(AndroidJUnit4::class)
class AddChoreTest {
    private val intent = Intent(ApplicationProvider.getApplicationContext(), MainActivity::class.java)

    @Rule(order = 0)
    @JvmField
    val disabledAnimations = DisableAnimationsRule()

    @Rule(order = 1)
    @JvmField
    val activityScenario = ActivityScenarioRule<MainActivity>(intent)

    companion object {
        private const val expectedUserName = "You"
        private const val expectedChoreName = "Colada"
        private val group = Group("UI_GROUP", "generic group", "EUR")
        private val user = User("UI_USER", "Gabriel", 0)
        private val user2 = User("UI_USER_2", "Raul Olmedo", 0)

        @JvmStatic
        @BeforeClass
        fun setUpFirebase() {
            val context = InstrumentationRegistry.getInstrumentation().targetContext
            FirebaseApp.initializeApp(context)
            runBlocking {
                GroupQueries().createGroup(group)
                UserQueries().createUser(user, group.id)
                UserQueries().createUser(user2, group.id)
            }

            // Sets shared preferences for activities to use
            val gr = context.getSharedPreferences(GROUP_ID, Context.MODE_PRIVATE)
            with (gr.edit()) {
                putString(GROUP_ID, group.id)
                commit()
            }

            val us = context.getSharedPreferences(USER_ID, Context.MODE_PRIVATE)
            with (us.edit()) {
                putString(USER_ID, user.id)
                commit()
            }
        }

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

    @Before
    fun pressFAB() {
        activityScenario.scenario.onActivity { it.keepScreenActive() }
        onView(withId(R.id.choreListFAB)).perform(click())
    }

    @Test
    fun verifyDataOfChoreIsCorrectlyDisplayedInListUponCreation() {
        val date = Calendar.getInstance()
        val expectedDate = UtilsSingleton.parseCalendarToStringOnList(date)

        onView(withId(R.id.choreDetailNameEditText)).perform(typeText(expectedChoreName))
        closeSoftKeyboard()
        onView(withId(R.id.choreDetailHigh)).perform(click())

        onView(withId(R.id.choreDetailExpireDateButton)).perform(click())
        onView(withClassName(equalTo(DatePicker::class.jvmName))).perform(PickerActions
            .setDate(date.get(Calendar.YEAR), date.get(Calendar.MONTH)+1, date.get(Calendar.DAY_OF_MONTH)))
        onView(withText("OK")).perform(click())

        onView(withId(R.id.choreDetailAssigneeButton)).perform(click())
        Thread.sleep(2000)
        onView(withId(R.id.usersList)).perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))

        onView(withId(R.id.toolbar_done)).perform(click())

        Thread.sleep(2000)
        // Verify chore name
        onView(withId(R.id.choreListRecyclerView)).check(matches(atPosition(0, hasDescendant(withText(expectedChoreName)))))
        // Verify importance
        onView(withId(R.id.choreListRecyclerView)).check(matches(atPosition(0, hasDescendant(matchesBackgroundColor(R.color.importanceHigh)))))
        // Verify expiration date
        onView(withId(R.id.choreListRecyclerView)).check(matches(atPosition(0, hasDescendant(withText(expectedDate)))))
        // Verify user name
        onView(withId(R.id.choreListRecyclerView)).check(matches(atPosition(0, hasDescendant(withText(expectedUserName)))))
    }
}