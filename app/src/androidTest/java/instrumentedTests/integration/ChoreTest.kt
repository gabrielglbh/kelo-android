package instrumentedTests.integration

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.platform.app.InstrumentationRegistry
import com.gabr.gabc.kelo.firebase.ChoreQueries
import com.gabr.gabc.kelo.firebase.GroupQueries
import com.gabr.gabc.kelo.firebase.UserQueries
import com.gabr.gabc.kelo.dataModels.Chore
import com.gabr.gabc.kelo.dataModels.Group
import com.gabr.gabc.kelo.dataModels.User
import com.gabr.gabc.kelo.viewModels.ChoreListViewModel
import com.google.firebase.FirebaseApp
import instrumentedTests.integration.utils.getOrAwaitValue
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.*
import org.junit.runner.RunWith
import org.junit.runners.BlockJUnit4ClassRunner
import java.util.*

/** Defines the Chore Instrumentation Tests */
@RunWith(BlockJUnit4ClassRunner::class)
class ChoreTest {
    @Rule
    @JvmField
    val liveDataRule = InstantTaskExecutorRule()

    private val choreListViewModel = ChoreListViewModel()

    private val q = ChoreQueries()
    private val group = Group("GROUP", "generic group", "EUR")
    private val user = User("USER", "generic user", 20)
    private val chore = Chore("CHORE_C", "CHORE_C", "", "USER", Calendar.getInstance().time, 30)

    /** Initializes and creates Firebase needed data for the tests */
    @Before
    fun setUp() {
        FirebaseApp.initializeApp(InstrumentationRegistry.getInstrumentation().context)
        runBlocking {
            GroupQueries().createGroup(group)
            UserQueries().createUser(user, group.id)
            ChoreQueries().createChore(chore, group.id)
        }
    }

    /** Cleans up Firebase */
    @After
    fun clean() {
        runBlocking {
            ChoreQueries().deleteAllChores(group.id)
            UserQueries().deleteAllUsers(group.id)
            GroupQueries().deleteGroup(group.id)
        }
    }

    /** Tests the createChore function */
    @Test
    fun createChoreSuccessfully() = runBlocking {
        val c = q.createChore(chore, group.id)
        assertTrue(c != null && c.id == chore.id)
    }

    /** Tests the createChore function when the group does not exist */
    @Test
    fun createChoreWhenGroupDoesNotExistSuccessfully() = runBlocking {
        val c = q.createChore(chore, "")
        assertTrue(c == null)
    }

    /** Tests the getChore function */
    @Test
    fun readChoreSuccessfully() = runBlocking {
        val result = q.getChore(chore.id!!, group.id)
        assertTrue(
            result != null &&
            result.id == chore.id
        )
    }

    /** Tests the getChore function when the group does not exist*/
    @Test
    fun readChoreNotSuccessfully() = runBlocking {
        val result = q.getChore("NON_EXISTING_CHORE", group.id)
        assertTrue(result == null)
    }

    /** Tests the getAllChores function for unfinished chores */
    @Test
    fun readAllChoresThatAreNotCompletedSuccessfully() = runBlocking {
        val uploadChore = Chore("CHORE_U", "Lavar los platos", "", "sadca09sd99aaa")
        q.createChore(uploadChore, group.id)
        val chores = q.getAllChores(group.id)
        assertTrue(chores != null)
        assertTrue(chores?.size == 2)
        assertTrue(chores!![0].expiration!!.time < chores[1].expiration!!.time)
    }

    /** Tests the getAllChores function for unfinished chores */
    @Test
    fun readAllChoresThatAreAlreadyCompletedSuccessfully() = runBlocking {
        val calendar = Calendar.getInstance()
        calendar.set(1997, 8, 22)
        val uploadChore1 = Chore("CHORE_U_COMPLETED", "Completeda 1", expiration = calendar.time, isCompleted = true)
        val uploadChore2 = Chore("SECOND_CHORE_COMPLETED", "Completeda 2", isCompleted = true)
        q.createChore(uploadChore1, group.id)
        q.createChore(uploadChore2, group.id)
        val chores = q.getAllChores(group.id, isCompleted = true)
        assertTrue(chores != null)
        assertTrue(chores?.size == 2)
        assertTrue(chores!![0].expiration!!.time < chores[1].expiration!!.time)
    }

    /** Tests the updateChore function */
    @Test
    fun updateChoreSuccessfully() = runBlocking {
        val modified = Chore(chore.id, "Lavar Platos", "", "Gabriel", Calendar.getInstance().time)
        val result = q.updateChore(modified, group.id)
        assertTrue(result)
    }

    /** Tests the updateChore function when the group does not exist */
    @Test
    fun updateChoreWhenGroupDoesNotExistSuccessfully() = runBlocking {
        val modified = Chore(chore.id, "Lavar Platos", "", "Gabriel", Calendar.getInstance().time)
        val result = q.updateChore(modified, "")
        assertFalse(result)
    }

    /** Tests the deleteChore function */
    @Test
    fun deleteChoreSuccessfully() = runBlocking {
        val result = q.deleteChore(chore.id!!, group.id)
        assertTrue(result)
    }

    /** Tests the deleteAllChores function */
    @Test
    fun deleteAllChoresSuccessfully() = runBlocking {
        val result = q.deleteAllChores(group.id)
        assertTrue(result)
    }

    /** Tests the deleteAllChores function when the group does not exist */
    @Test
    fun deleteAllChoresWhenGroupDoesNotExistSuccessfully() = runBlocking {
        val result = q.deleteAllChores("")
        assertFalse(result)
    }

    /** Tests the completeChore function */
    @Test
    fun completeChoreSuccessfully() = runBlocking {
        val success = q.completeChore(chore, group.id)
        val user = UserQueries().getUser(user.id, group.id)
        val c = chore.id?.let { q.getChore(it, group.id) }
        assertTrue(success && user != null && user.points == 50 && c != null && c.isCompleted)
    }

    /** Tests the completeChore function when the user does not exist */
    @Test
    fun completeChoreWhenUserDoesNotExistSuccessfully() = runBlocking {
        val c = q.createChore(Chore("NEW_CHORE", "Tidy Up", "", ""), group.id)
        val success = c?.let { q.completeChore(it, group.id) }
        assertTrue(success != null && !success)
    }

    /** Tests that the view model of chore list is functioning well for controlling the completed-non-completed chores */
    @Test
    fun choreListViewModelControlOverCompletedChores() {
        choreListViewModel.setShowCompleted(false)
        assertTrue(!choreListViewModel.showCompleted.getOrAwaitValue())
    }

    /** Tests that the view model of chore list is functioning well for the initialization of the list */
    @Test
    fun choreListViewModelControlOverListOfChores() {
        val chores = arrayListOf(chore, chore, chore)
        choreListViewModel.addAllChores(chores)
        assertTrue(choreListViewModel.choreList.getOrAwaitValue().size == 3 &&
                choreListViewModel.choreList.getOrAwaitValue() == chores)
    }
}