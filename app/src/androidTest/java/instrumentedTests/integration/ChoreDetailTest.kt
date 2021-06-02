package instrumentedTests.integration

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.platform.app.InstrumentationRegistry
import com.gabr.gabc.kelo.choreDetail.AssigneeViewModel
import com.gabr.gabc.kelo.constants.Constants
import com.gabr.gabc.kelo.models.User
import com.gabr.gabc.kelo.utils.SharedPreferences
import instrumentedTests.integration.utils.getOrAwaitValue
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.BlockJUnit4ClassRunner

/** Defines the Chore Details Instrumentation Tests */
@RunWith(BlockJUnit4ClassRunner::class)
class ChoreDetailTest {
    @Rule
    @JvmField
    val liveDataRule = InstantTaskExecutorRule()

    private val assigneeModel = AssigneeViewModel()

    companion object {
        private lateinit var context: Context
        private const val userId = "verygenericuidfromfb"

        /** Sets up the shared preferences for the tests to work */
        @JvmStatic
        @BeforeClass
        fun setUpSharedPreferences() {
            context = InstrumentationRegistry.getInstrumentation().targetContext

            val us = context.getSharedPreferences(Constants.USER_ID, Context.MODE_PRIVATE)
            with (us.edit()) {
                putString(Constants.USER_ID, userId)
                commit()
            }
            us.getString(Constants.USER_ID, "")?.let { SharedPreferences.userId = it }
        }
    }

    /** Tests the assigneeModel class */
    @Test
    fun assigneeModelUpdatesWithNewUser() {
        val user = User("", "Gabriel Garcia", 45)
        assigneeModel.setAssignee(user)
        assertTrue(assigneeModel.assignee.getOrAwaitValue() == user)
    }

    /** Tests the isUserBeingDisplayedCurrentUser function */
    @Test
    fun assigneeIsCurrentUser() {
        val isCurrentUser = SharedPreferences.isUserBeingDisplayedCurrentUser(userId)
        assertTrue(isCurrentUser)
    }

    /** Tests the isUserBeingDisplayedCurrentUser function */
    @Test
    fun assigneeIsOtherUserInGroup() {
        val isCurrentUser = SharedPreferences.isUserBeingDisplayedCurrentUser("otheruserbeingdisplayed")
        assertFalse(isCurrentUser)
    }
}