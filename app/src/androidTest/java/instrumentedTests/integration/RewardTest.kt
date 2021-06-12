package instrumentedTests.integration

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.platform.app.InstrumentationRegistry
import com.gabr.gabc.kelo.dataModels.Group
import com.gabr.gabc.kelo.dataModels.Reward
import com.gabr.gabc.kelo.dataModels.User
import com.gabr.gabc.kelo.firebase.GroupQueries
import com.gabr.gabc.kelo.firebase.RewardQueries
import com.gabr.gabc.kelo.firebase.UserQueries
import com.gabr.gabc.kelo.viewModels.RewardViewModel
import com.google.firebase.FirebaseApp
import instrumentedTests.integration.utils.getOrAwaitValue
import junit.framework.TestCase
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.BlockJUnit4ClassRunner
import java.util.*

/** Defines the Reward Instrumentation Tests */
@RunWith(BlockJUnit4ClassRunner::class)
class RewardTest {
    @Rule
    @JvmField
    val liveDataRule = InstantTaskExecutorRule()

    private var rewardViewModel = RewardViewModel()

    private val q = RewardQueries()
    private val group = Group("GROUP", "generic group", "EUR")
    private val reward = Reward("REWARD", "generic reward", expiration = Calendar.getInstance().time, frequency = 2)

    /** Initializes and creates Firebase needed data for the tests */
    @Before
    fun setUp() {
        FirebaseApp.initializeApp(InstrumentationRegistry.getInstrumentation().context)
        runBlocking {
            GroupQueries().createGroup(group)
            q.createReward(reward, group.id)
        }
    }

    /** Cleans up Firebase */
    @After
    fun clean() {
        runBlocking {
            RewardQueries().deleteAllRewards(group.id)
            GroupQueries().deleteGroup(group.id)
        }
    }

    /** Tests the createReward function */
    @Test
    fun createRewardSuccessfully() = runBlocking {
        val r = q.createReward(reward, group.id)
        assertTrue(r != null && reward.id == r.id)
    }

    /** Tests the createReward function on negative outcome */
    @Test
    fun createRewardSuccessfullyWhenGroupDoesNotExist() = runBlocking {
        val r = q.createReward(reward, "")
        assertTrue(r == null)
    }

    /** Tests the getAllRewards function */
    @Test
    fun readAllRewardSuccessfully() = runBlocking {
        val result = q.getAllRewards(group.id)
        assertTrue(result != null && result.size == 1)
    }

    /** Tests the updateReward function */
    @Test
    fun updateRewardSuccessfully() = runBlocking {
        val modified = Reward(reward.id, "Gabriel")
        val result = q.updateReward(modified, group.id)
        assertTrue(result)
    }

    /** Tests the updateReward function when the group does not exist */
    @Test
    fun updateRewardWhenGroupDoesNotExistSuccessfully() = runBlocking {
        val modified = Reward(reward.id, "Gabriel")
        val result = q.updateReward(modified, "")
        assertFalse(result)
    }

    /** Tests the deleteReward function */
    @Test
    fun deleteRewardSuccessfully() = runBlocking {
        val result = q.deleteReward(reward.id!!, group.id)
        assertTrue(result)
    }

    /** Tests the deleteReward function when the user does not exist  */
    @Test
    fun deleteRewardWhenUserDoesNotExistSuccessfully() = runBlocking {
        val result = q.deleteReward("", group.id)
        assertFalse(result)
    }

    /** Tests the deleteAllRewards function */
    @Test
    fun deleteAllRewardsSuccessfully() = runBlocking {
        val result = q.deleteAllRewards(group.id)
        assertTrue(result)
    }

    /** Tests the deleteAllRewards function when the group does not exist */
    @Test
    fun deleteAllRewardsWhenGroupDoesNotExistSuccessfully() = runBlocking {
        val result = q.deleteAllRewards("")
        assertFalse(result)
    }

    /** Tests the rewardViewModel class for the frequency selection */
    @Test
    fun rewardViewModelUpdatesWithSelectionOfFrequency() {
        val newFrequency = Reward.Frequencies.EVERY_TWO_WEEKS
        rewardViewModel.setPeriodicity(newFrequency)
        assertTrue(rewardViewModel.periodicity.getOrAwaitValue() == newFrequency)
    }
}