package instrumentedTests.integration

import androidx.test.platform.app.InstrumentationRegistry
import com.gabr.gabc.kelo.firebase.ChoreQueries
import com.gabr.gabc.kelo.firebase.GroupQueries
import com.gabr.gabc.kelo.firebase.UserQueries
import com.gabr.gabc.kelo.models.Chore
import com.gabr.gabc.kelo.models.Group
import com.gabr.gabc.kelo.models.User
import com.google.firebase.FirebaseApp
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.*
import org.junit.runner.RunWith
import org.junit.runners.BlockJUnit4ClassRunner
import java.util.*

/** Defines the Chore Instrumentation Tests */
@RunWith(BlockJUnit4ClassRunner::class)
class ChoreTest {
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
        val chore = q.createChore(chore, group.id)
        assertTrue(chore != null && chore.id == chore.id)
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

    /** Tests the updateChore function */
    @Test
    fun updateChoreSuccessfully() = runBlocking {
        val modified = Chore(chore.id, "Lavar Platos", "", "Gabriel", Calendar.getInstance().time)
        val result = q.updateChore(modified, group.id)
        assertTrue(result)
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

    /** Tests the completeChore function */
    @Test
    fun completeChoreSuccessfully() = runBlocking {
        val success = q.completeChore(chore, group.id)
        val user = UserQueries().getUser(user.id, group.id)
        assertTrue(success && user != null && user.points == 50)
    }

    /** Tests the listener function for chores: attachListenerToChores - Addition */
    @Test
    fun setListenerOnChoresAddSuccessfully() = runBlocking {
        val chores = arrayListOf<Chore>()
        val uploadChore = Chore("CHORE_U", "Lavar los platos", "", "sadca09sd99aaa",
            Calendar.getInstance().time)
        val result = q.attachListenerToChores(group.id, { pos, c ->
            chores.add(pos, c)
        }, { _, _ -> }, { })
        q.createChore(uploadChore, group.id)
        assertTrue(result != null)
        assertTrue(chores.size == 2)
        assertTrue(chores[0].expiration!!.time < chores[1].expiration!!.time)
        assertTrue(chores.indexOf(uploadChore) == 1)
    }

    /** Tests the listener function for chores: attachListenerToChores - Update */
    @Test
    fun setListenerOnChoresUpdateSuccessfully() = runBlocking {
        val chores = arrayListOf(chore)
        val uploadChore = Chore(chore.id, "CHORE_U", "", "asiduhfoasdhfas998",
            Calendar.getInstance().time)
        val result = q.attachListenerToChores(group.id, { _, _ -> }, { pos, c ->
            chores[pos] = c
        }, { })
        q.updateChore(uploadChore, group.id)
        assertTrue(result != null)
        assertTrue(chores.size == 1)
        assertTrue(chores.indexOf(uploadChore) == 0)
        assertTrue(chore != uploadChore)
    }

    /** Tests the listener function for chores: attachListenerToChores - Deletion */
    @Test
    fun setListenerOnChoresDeleteSuccessfully() = runBlocking {
        val chores = arrayListOf<Chore>()
        val uploadChore = Chore("CHORE_U", "Lavar los platos", "", "sadca09sd99aaa",
            Calendar.getInstance().time)
        val result = q.attachListenerToChores(group.id, { pos, c ->
            chores.add(pos, c)
        }, { _, _ -> }, { pos ->
            chores.removeAt(pos)
        })
        q.createChore(uploadChore, group.id)
        q.deleteChore(uploadChore.id!!, group.id)
        assertTrue(result != null)
        assertTrue(chores.size == 1)
    }
}