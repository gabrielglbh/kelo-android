package instrumentedTests.integration

import androidx.test.platform.app.InstrumentationRegistry
import com.gabr.gabc.kelo.firebase.GroupQueries
import com.gabr.gabc.kelo.firebase.UserQueries
import com.gabr.gabc.kelo.dataModels.Group
import com.gabr.gabc.kelo.dataModels.User
import com.google.firebase.FirebaseApp
import junit.framework.TestCase.assertFalse
import kotlinx.coroutines.runBlocking
import org.junit.*
import org.junit.Assert.assertTrue
import org.junit.runner.RunWith
import org.junit.runners.BlockJUnit4ClassRunner

/** Defines the Group Instrumentation Tests */
@RunWith(BlockJUnit4ClassRunner::class)
class GroupTest {
    private val q = GroupQueries()
    private val group = Group("GROUP", "generic group", "EUR")

    /** Initializes and creates Firebase needed data for the tests */
    @Before
    fun setUp() {
        FirebaseApp.initializeApp(InstrumentationRegistry.getInstrumentation().context)
        runBlocking {
            GroupQueries().createGroup(group)
        }
    }

    /** Cleans up Firebase */
    @After
    fun clean() {
        runBlocking {
            GroupQueries().deleteGroup(group.id)
        }
    }

    /** Tests the createGroup function */
    @Test
    fun createGroupSuccessfully() = runBlocking {
        val result = q.createGroup(group)
        assertTrue(result != null && result.id == group.id)
    }

    /** Tests the getGroup function */
    @Test
    fun readGroupSuccessfully() = runBlocking {
        val result = q.getGroup(group.id)
        assertTrue(
            result != null &&
            result.name == group.name &&
            result.currency == group.currency
        )
    }

    /** Tests the getGroup function when the group does not exist */
    @Test
    fun readGroupNotSuccessfully() = runBlocking {
        val result = q.getGroup("NON_EXISTING_GROUP")
        assertTrue(result == null)
    }

    /** Tests the updateGroup function */
    @Test
    fun updateGroupSuccessfully() = runBlocking {
        val modified = Group(group.id, "updated", "JPY")
        val result = q.updateGroup(modified)
        assertTrue(result)
    }

    /** Tests the deleteGroup function */
    @Test
    fun deleteGroupSuccessfully() = runBlocking {
        val result = q.deleteGroup(group.id)
        assertTrue(result)
    }

    /** Tests the deleteGroup function when group does not exist */
    @Test
    fun deleteGroupWhenGroupDoesNotExistSuccessfully() = runBlocking {
        val result = q.deleteGroup("")
        assertFalse(result)
    }

    /** Tests the checkGroupAvailability function */
    @Test
    fun checkGroupAvailabilitySuccessfully() = runBlocking {
        val result = q.checkGroupAvailability(group.id)
        assertTrue(result == 0)
    }

    /** Tests the checkGroupAvailability function when group is full */
    @Test
    fun checkGroupAvailabilityWhenGroupIsFullSuccessfully() = runBlocking {
        UserQueries().createUser(User("USER_1", "Gabriel"), group.id)
        UserQueries().createUser(User("USER_2", "Raul"), group.id)
            val result = q.checkGroupAvailability(group.id, maxUsers = 2)
            assertTrue(result == -2)
        }

    /** Tests the checkGroupAvailability function when group is non existing */
    @Test
    fun checkGroupAvailabilityWhenGroupDoesNotExistSuccessfully() = runBlocking {
        val result = q.checkGroupAvailability("NO_GROUP")
        assertTrue(result == -3)
    }
}