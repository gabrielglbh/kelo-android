package instrumentedTests.integration

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.gabr.gabc.kelo.constants.CURRENCIES
import com.gabr.gabc.kelo.welcomeActivity.WelcomeViewModel
import instrumentedTests.integration.utils.getOrAwaitValue
import junit.framework.TestCase
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.BlockJUnit4ClassRunner

@RunWith(BlockJUnit4ClassRunner::class)
class CurrencyTest {
    @Rule
    @JvmField
    val liveDataRule = InstantTaskExecutorRule()

    private val welcomeModel = WelcomeViewModel()

    @Test
    fun welcomeModelEURIsDefaultCurrency() {
        TestCase.assertTrue(welcomeModel.groupCurrency.getOrAwaitValue() == CURRENCIES[0])
    }

    @Test
    fun welcomeModelUpdatesWithNewCurrency() {
        val currency = CURRENCIES[8]
        welcomeModel.setCurrency(currency)
        TestCase.assertTrue(welcomeModel.groupCurrency.getOrAwaitValue() == currency)
    }
}