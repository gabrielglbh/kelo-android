package instrumentedTests.integration

import androidx.test.platform.app.InstrumentationRegistry
import com.gabr.gabc.kelo.firebase.GroupQueries
import com.gabr.gabc.kelo.models.Group
import com.google.firebase.FirebaseApp
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

    /** Tests the checkGroupAvailability function */
    @Test
    fun checkGroupAvailabilitySuccessfully() = runBlocking {
        val result = q.checkGroupAvailability(group.id)
        assertTrue(result == 0)
    }
}