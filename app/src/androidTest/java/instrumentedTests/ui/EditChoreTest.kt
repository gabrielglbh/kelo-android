package instrumentedTests.ui

import android.content.Context
import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
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
import com.gabr.gabc.kelo.models.Chore
import com.gabr.gabc.kelo.models.Group
import com.gabr.gabc.kelo.models.User
import com.gabr.gabc.kelo.utils.UtilsSingleton
import com.google.firebase.FirebaseApp
import instrumentedTests.ui.utils.DisableAnimationsRule
import instrumentedTests.ui.utils.keepScreenActive
import kotlinx.coroutines.runBlocking
import org.junit.*
import org.junit.runner.RunWith
import java.util.*

/** Defines the Edit Chore UI Test */
@RunWith(AndroidJUnit4::class)
class EditChoreTest {
    private val intent = Intent(ApplicationProvider.getApplicationContext(), MainActivity::class.java)

    @Rule(order = 0)
    @JvmField
    val disabledAnimations = DisableAnimationsRule()

    @Rule(order = 1)
    @JvmField
    val activityScenario = ActivityScenarioRule<MainActivity>(intent)

    companion object {
        private val group = Group("UI_GROUP", "generic group", "EUR")
        private val user = User("UI_USER", "Gabriel", 0)
        private val chore = Chore("CHORE_C", "CHORE_C", "", "UI_USER", Calendar.getInstance().time, 20)

        /** Initializes and creates Firebase needed data for the tests */
        @JvmStatic
        @BeforeClass
        fun setUpFirebase() {
            val context = InstrumentationRegistry.getInstrumentation().targetContext
            FirebaseApp.initializeApp(context)
            runBlocking {
                GroupQueries().createGroup(group)
                ChoreQueries().createChore(chore, group.id)
                UserQueries().createUser(user, group.id)
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

        /** Cleans Up Firebase */
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

    /** Function called before each test to keep the screen active */
    @Before
    fun setUp() { activityScenario.scenario.onActivity { it.keepScreenActive() } }

    /** Tests that the view details chore whole routine works perfectly */
    @Test
    fun verifyDataOfChoreIsCorrectlyDisplayedOnEditChore() {
        val date = Calendar.getInstance()
        val expectedDate = UtilsSingleton.parseCalendarToString(date)

        Thread.sleep(2000)
        onView(withId(R.id.choreListRecyclerView)).perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))

        // Verify Selected Chore is populated
        onView(withId(R.id.choreDetailNameEditText)).check(ViewAssertions.matches(withText(chore.name)))
        onView(withId(R.id.choreDetailAssigneeButton)).check(ViewAssertions.matches(withText("YOU")))
        onView(withId(R.id.choreDetailMedium)).check(ViewAssertions.matches(isChecked()))
        onView(withId(R.id.choreDetailExpireDateButton)).check(ViewAssertions.matches(withText(expectedDate)))
    }
}