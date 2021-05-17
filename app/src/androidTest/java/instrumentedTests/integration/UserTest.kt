package instrumentedTests.integration

import androidx.test.platform.app.InstrumentationRegistry
import com.gabr.gabc.kelo.firebase.GroupQueries
import com.gabr.gabc.kelo.firebase.UserQueries
import com.gabr.gabc.kelo.models.Group
import com.gabr.gabc.kelo.models.User
import com.google.firebase.FirebaseApp
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.*
import org.junit.runner.RunWith
import org.junit.runners.BlockJUnit4ClassRunner

@RunWith(BlockJUnit4ClassRunner::class)
class UserTest {
    private val q = UserQueries()
    private val group = Group("GROUP", "generic group", "EUR")
    private val user = User("CREATE_USER", "createUser", 0)

    @Before
    fun setUp() {
        FirebaseApp.initializeApp(InstrumentationRegistry.getInstrumentation().context)
        runBlocking {
            GroupQueries().createGroup(group)
            UserQueries().createUser(user, group.id)
        }
    }
    @After
    fun clean() {
        runBlocking {
            UserQueries().deleteAllUsers(group.id)
            GroupQueries().deleteGroup(group.id)
        }
    }

    @Test
    fun createUserSuccessfully() = runBlocking {
        val user = q.createUser(user, group.id)
        assertTrue(user != null && user.id == user.id)
    }

    @Test
    fun readUserSuccessfully() = runBlocking {
        val result = q.getUser(user.id, group.id)
        assertTrue(
            result != null &&
            result.id == user.id
        )
    }

    @Test
    fun readMostLazyUserSuccessfully() = runBlocking {
        val u = User("CREATE_USER_2", "createUser", 30)
        val notToBeRetrievedUser = q.createUser(u, group.id)
        assertTrue(notToBeRetrievedUser != null)
        val result = q.getMostLazyUser(group.id)
        assertTrue(
            result != null &&
            result.id == user.id
        )
    }

    @Test
    fun readRandomUserSuccessfully() = runBlocking {
        q.createUser(User("RND_USER", "Random", 30), group.id)
        val users = q.getAllUsers(group.id)
        val rndUser = q.getRandomUser(users)
        assertTrue(rndUser != null)
    }

    @Test
    fun readAllUsersSuccessfully() = runBlocking {
        val result = q.getAllUsers(group.id)
        assertTrue(result != null && result.size == 1)
    }

    @Test
    fun updateUserSuccessfully() = runBlocking {
        val modified = User(user.id, "Gabriel", 56)
        val result = q.updateUser(modified, group.id)
        assertTrue(result)
    }

    @Test
    fun deleteUserSuccessfully() = runBlocking {
        val result = q.deleteUser(user.id, group.id)
        assertTrue(result)
    }

    @Test
    fun deleteAllUsersSuccessfully() = runBlocking {
        val result = q.deleteAllUsers(group.id)
        assertTrue(result)
    }

    @Test
    fun joinGroupSuccessfully() = runBlocking {
        val join = User("JOIN_USER", "joining user", 0)
        val result = q.joinGroup(group.id, join)
        assertTrue(result == join.id)
    }

    @Test
    fun isUsernameAvailableSuccessfully() = runBlocking {
        val result = q.isUsernameAvailable(group.id, "nameIsFullyAvailable")
        assertTrue(result)
    }
}