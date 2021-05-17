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

@RunWith(BlockJUnit4ClassRunner::class)
class ChoreTest {
    private val q = ChoreQueries()
    private val group = Group("GROUP", "generic group", "EUR")
    private val user = User("USER", "generic user", 0)
    private val chore = Chore("CHORE_C", "CHORE_C", "", "USER", Calendar.getInstance().time, 30)

    @Before
    fun setUp() {
        FirebaseApp.initializeApp(InstrumentationRegistry.getInstrumentation().context)
        runBlocking {
            GroupQueries().createGroup(group)
            UserQueries().createUser(user, group.id)
            ChoreQueries().createChore(chore, group.id)
        }
    }
    @After
    fun clean() {
        runBlocking {
            ChoreQueries().deleteAllChores(group.id)
            UserQueries().deleteAllUsers(group.id)
            GroupQueries().deleteGroup(group.id)
        }
    }

    @Test
    fun createChoreSuccessfully() = runBlocking {
        val chore = q.createChore(chore, group.id)
        assertTrue(chore != null && chore.id == chore.id)
    }

    @Test
    fun readChoreSuccessfully() = runBlocking {
        val result = q.getChore(chore.id!!, group.id)
        assertTrue(
            result != null &&
            result.id == chore.id
        )
    }

    @Test
    fun updateChoreSuccessfully() = runBlocking {
        val modified = Chore(chore.id, "Lavar Platos", "", "Gabriel", Calendar.getInstance().time)
        val result = q.updateChore(modified, group.id)
        assertTrue(result)
    }

    @Test
    fun deleteChoreSuccessfully() = runBlocking {
        val result = q.deleteChore(chore.id!!, group.id)
        assertTrue(result)
    }

    @Test
    fun deleteAllChoresSuccessfully() = runBlocking {
        val result = q.deleteAllChores(group.id)
        assertTrue(result)
    }

    @Test
    fun completeChoreSuccessfully() = runBlocking {
        val success = q.completeChore(chore, group.id)
        val user = UserQueries().getUser(user.id, group.id)
        assertTrue(success && user != null && user.points == 30)
    }

    @Test
    fun setListenerOnChoresAddSuccessfully() = runBlocking {
        val chores = arrayListOf<Chore>()
        val uploadChore = Chore("CHORE_U", "CHORE_U", "", "sadca09sd99aaa", Calendar.getInstance().time)
        val result = q.attachListenerToChores(group.id, { pos, c ->
            chores.add(pos, c)
        }, { _, _ -> }, { })
        q.createChore(uploadChore, group.id)
        assertTrue(result != null)
        assertTrue(chores.size == 2)
        assertTrue(chores.indexOf(uploadChore) == 1)
    }

    @Test
    fun setListenerOnChoresUpdateSuccessfully() = runBlocking {
        val chores = arrayListOf(chore)
        val uploadChore = Chore(chore.id, "CHORE_U", "", "asiduhfoasdhfas998", Calendar.getInstance().time)
        val result = q.attachListenerToChores(group.id, { _, _ -> }, { pos, c ->
            chores[pos] = c
        }, { })
        q.updateChore(uploadChore, group.id)
        assertTrue(result != null)
        assertTrue(chores.size == 1)
        assertTrue(chores.indexOf(uploadChore) == 0)
        assertTrue(chore != uploadChore)
    }

    @Test
    fun setListenerOnChoresDeleteSuccessfully() = runBlocking {
        val chores = arrayListOf<Chore>()
        val uploadChore = Chore("CHORE_U", "CHORE_U", "", "sadca09sd99aaa", Calendar.getInstance().time)
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