package instrumentedTests.integration

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.gabr.gabc.kelo.constants.Constants
import com.gabr.gabc.kelo.welcome.WelcomeViewModel
import instrumentedTests.integration.utils.getOrAwaitValue
import junit.framework.TestCase
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.BlockJUnit4ClassRunner

/** Defines the Currency Instrumentation Tests */
@RunWith(BlockJUnit4ClassRunner::class)
class CurrencyTest {
    @Rule
    @JvmField
    val liveDataRule = InstantTaskExecutorRule()

    private val welcomeModel = WelcomeViewModel()

    /** Tests the welcomeModel class initializer */
    @Test
    fun welcomeModelEURIsDefaultCurrency() {
        TestCase.assertTrue(welcomeModel.groupCurrency.getOrAwaitValue() == Constants.CURRENCIES[0])
    }

    /** Tests the welcomeModel class */
    @Test
    fun welcomeModelUpdatesWithNewCurrency() {
        val currency = Constants.CURRENCIES[8]
        welcomeModel.setCurrency(currency)
        TestCase.assertTrue(welcomeModel.groupCurrency.getOrAwaitValue() == currency)
    }
}