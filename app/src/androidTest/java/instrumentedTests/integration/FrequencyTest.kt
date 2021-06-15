package instrumentedTests.integration

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.gabr.gabc.kelo.dataModels.Reward
import com.gabr.gabc.kelo.viewModels.RewardViewModel
import instrumentedTests.integration.utils.getOrAwaitValue
import junit.framework.TestCase.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.BlockJUnit4ClassRunner
import java.util.*

/** Defines the Frequency (Reward) Instrumentation Tests */
@RunWith(BlockJUnit4ClassRunner::class)
class FrequencyTest {
    @Rule
    @JvmField
    val liveDataRule = InstantTaskExecutorRule()

    private val rewardViewModel = RewardViewModel()

    /** Tests the rewardViewModel class */
    @Test
    fun rewardViewModelUpdatesWithNewFrequency() {
        val freq = Reward.Frequencies.MONTHLY
        val calendar = Calendar.getInstance()
        val today = Calendar.getInstance()
        Reward.Frequencies.getDateFromMode(freq)?.let { calendar.time = it }
        today.add(Calendar.MONTH, 1)
        rewardViewModel.setPeriodicity(freq)
        assertTrue(calendar.get(Calendar.MONTH) == today.get(Calendar.MONTH) &&
                rewardViewModel.periodicity.getOrAwaitValue() == Reward.Frequencies.MONTHLY)
    }

    /** Tests the rewardViewModel class with no frequency */
    @Test
    fun rewardViewModelUpdatesWithNoFrequency() {
        rewardViewModel.setPeriodicity(Reward.Frequencies.NO_FREQUENCY)
        assertTrue(rewardViewModel.periodicity.getOrAwaitValue() == Reward.Frequencies.NO_FREQUENCY)
    }
}