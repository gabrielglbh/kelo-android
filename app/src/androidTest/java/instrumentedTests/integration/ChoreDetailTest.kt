package instrumentedTests.integration

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.platform.app.InstrumentationRegistry
import com.gabr.gabc.kelo.choreDetailActivity.AssigneeViewModel
import com.gabr.gabc.kelo.constants.USER_ID
import com.gabr.gabc.kelo.models.User
import com.gabr.gabc.kelo.utils.SharedPreferences
import com.gabr.gabc.kelo.utils.UtilsSingleton
import instrumentedTests.integration.utils.getOrAwaitValue
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.BlockJUnit4ClassRunner

@RunWith(BlockJUnit4ClassRunner::class)
class ChoreDetailTest {
    @Rule
    @JvmField
    val liveDataRule = InstantTaskExecutorRule()

    private val assigneeModel = AssigneeViewModel()

    companion object {
        private lateinit var context: Context
        private const val userId = "verygenericuidfromfb"

        @JvmStatic
        @BeforeClass
        fun setUpSharedPreferences() {
            context = InstrumentationRegistry.getInstrumentation().targetContext

            val us = context.getSharedPreferences(USER_ID, Context.MODE_PRIVATE)
            with (us.edit()) {
                putString(USER_ID, userId)
                commit()
            }
            us.getString(USER_ID, "")?.let { SharedPreferences.userId = it }
        }
    }

    @Test
    fun assigneeModelUpdatesWithNewUser() {
        val user = User("", "Gabriel Garcia", 45)
        assigneeModel.setAssignee(user)
        assertTrue(assigneeModel.assignee.getOrAwaitValue() == user)
    }

    @Test
    fun assigneeIsCurrentUser() {
        val isCurrentUser = UtilsSingleton.isUserBeingDisplayedCurrentUser(userId)
        assertTrue(isCurrentUser)
    }

    @Test
    fun assigneeIsOtherUserInGroup() {
        val isCurrentUser = UtilsSingleton.isUserBeingDisplayedCurrentUser("otheruserbeingdisplayed")
        assertFalse(isCurrentUser)
    }
}