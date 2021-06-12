package instrumentedTests.ui

import android.content.Context
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.gabr.gabc.kelo.R
import com.gabr.gabc.kelo.constants.Constants
import com.gabr.gabc.kelo.dataModels.Group
import com.gabr.gabc.kelo.dataModels.Reward
import com.gabr.gabc.kelo.dataModels.User
import com.gabr.gabc.kelo.firebase.GroupQueries
import com.gabr.gabc.kelo.firebase.RewardQueries
import com.gabr.gabc.kelo.firebase.UserQueries
import com.gabr.gabc.kelo.main.MainActivity
import com.google.firebase.FirebaseApp
import instrumentedTests.ui.utils.DisableAnimationsRule
import instrumentedTests.ui.utils.keepScreenActive
import kotlinx.coroutines.runBlocking
import org.junit.*
import org.junit.runner.RunWith
import java.util.*

/** Defines the Edit Reward UI Test */
@RunWith(AndroidJUnit4::class)
class EditRewardTest {
    private val intent = Intent(ApplicationProvider.getApplicationContext(), MainActivity::class.java)

    @Rule(order = 0)
    @JvmField
    val disabledAnimations = DisableAnimationsRule()

    @Rule(order = 1)
    @JvmField
    val activityScenario = ActivityScenarioRule<MainActivity>(intent)

    companion object {
        private val context = InstrumentationRegistry.getInstrumentation().targetContext
        private val group = Group("UI_GROUP", "generic group", "EUR")
        private val user = User("UI_USER", "Gabriel", 0, isAdmin = true)
        private val reward = Reward("UI_REWARD", "Cervezas con Olmedo", Calendar.getInstance().time, frequency = 1)

        /** Initializes and creates Firebase needed data for the tests */
        @JvmStatic
        @BeforeClass
        fun setUpFirebase() {
            FirebaseApp.initializeApp(context)
            runBlocking {
                GroupQueries().createGroup(group)
                UserQueries().createUser(user, group.id)
                RewardQueries().createReward(reward, group.id)
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

        /** Cleans Up Firebase */
        @JvmStatic
        @AfterClass
        fun cleanFirebase() {
            runBlocking {
                UserQueries().deleteAllUsers(group.id)
                GroupQueries().deleteGroup(group.id)
                RewardQueries().deleteAllRewards(group.id)
            }
        }
    }

    /** Function called before each test to keep the screen active */
    @Before
    fun setUp() {
        activityScenario.scenario.onActivity { it.keepScreenActive() }
        onView(withId(R.id.settings_menu)).perform(click())
    }

    /** Tests that the view details reward whole routine works perfectly */
    @Test
    fun verifyDataOfRewardIsCorrectlyDisplayedOnEditReward() {
        Thread.sleep(2000)
        onView(withId(R.id.rewardParent)).perform(scrollTo()).perform(click())
        
        // Verify Selected Reward is populated
        onView(withId(R.id.rewardDetailNameLayout)).check(matches(hasDescendant(withText(reward.name))))
        onView(withId(R.id.rewardDetailPeriodicityButton)).check(matches(
            withText(Reward.Frequencies.getStringFromMode(context, 1))))
    }
}