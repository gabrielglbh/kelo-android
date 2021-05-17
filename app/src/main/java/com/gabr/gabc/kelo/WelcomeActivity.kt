package com.gabr.gabc.kelo

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.gabr.gabc.kelo.constants.JOIN_GROUP
import com.gabr.gabc.kelo.utils.UtilsSingleton
import com.gabr.gabc.kelo.welcomeActivity.WelcomeViewModel
import com.gabr.gabc.kelo.welcomeActivity.viewPager.ViewPagerPage1
import com.gabr.gabc.kelo.welcomeActivity.viewPager.ViewPagerPage2
import com.gabr.gabc.kelo.welcomeActivity.viewPager.ViewPagerPage3

class WelcomeActivity : AppCompatActivity() {

    private lateinit var parent: ConstraintLayout
    private lateinit var viewPager: ViewPager2
    private lateinit var backButton: View
    private lateinit var fullViewLoading: ConstraintLayout
    private lateinit var loading: ProgressBar

    private var onBackCallback: OnBackPressedCallback? = null

    private lateinit var viewModel: WelcomeViewModel

    /**
     * [FragmentStateAdapter] for the ViewPager2 to show all the Fragments
     * */
    private inner class ScreenSlidePagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
        override fun getItemCount(): Int = 3
        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> ViewPagerPage1()
                1 -> ViewPagerPage2()
                2 -> ViewPagerPage3()
                else -> ViewPagerPage3()
            }
        }
    }

    /**
     * Initialize all views from the layout
     * [parent] serves for the purpose of hiding the keyboard when touched outside of any EditText
     * [viewPager] sets the ViewPager2 adapter with [ScreenSlidePagerAdapter]
     *
     * @param savedInstanceState: current bundle, if any
     * */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        viewModel = run { ViewModelProvider(this).get(WelcomeViewModel::class.java) }
        UtilsSingleton.changeStatusBarColor(this, this, R.color.contrast)

        parent = findViewById(R.id.welcomePageConstraintLayout)
        parent.setOnClickListener { UtilsSingleton.hideKeyboard(this, parent) }

        backButton = findViewById(R.id.welcomeBackButton)
        backButton.setOnClickListener { viewModel.setPagerPage(viewModel.getCurrentPage() - 1) }

        fullViewLoading = findViewById(R.id.loadingFragment)
        loading = findViewById(R.id.loadingWidget)

        setUpViewPager()
        setUpObserverLiveData()
        handleDeepLinkIntent(intent)
    }

    /**
     * Receiver for new intents, most likely for Deep Links: handleDeepLinkIntent
     * */
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent?.let { handleDeepLinkIntent(intent) }
    }

    /**
     * Sets up the ViewPager logic
     * */
    private fun setUpViewPager() {
        viewPager = findViewById(R.id.welcomePager)
        viewPager.adapter = ScreenSlidePagerAdapter(this)
        viewPager.isUserInputEnabled = false
    }

    /**
     * Sets up the observer for the ViewModel for changing pages when needed and for showing the
     * loading widget when updating Firebase
     * */
    private fun setUpObserverLiveData() {
        viewModel.viewPagerMode.observe(this, { mode ->
            viewPager.setCurrentItem(mode, true)
            if (mode != 0) {
                backButton.visibility = View.VISIBLE
                setUpOnBackCallback()
            } else {
                // Remove the callback only if we are in the initial page
                backButton.visibility = View.GONE
                removeOnBackCallback(onBackCallback)
            }
        })
        viewModel.isLoading.observe(this, { loading ->
            UtilsSingleton.setUserInteractionWhileLoading(this, parent, loading)
            UtilsSingleton.manageLoadingView(this.loading, fullViewLoading, loading)
            if (loading) UtilsSingleton.changeStatusBarColor(this, this, R.color.secondaryColor)
            else UtilsSingleton.changeStatusBarColor(this, this, R.color.contrast)
        })
    }

    /**
     * Handles the deep link interaction for the Share Code to Join group.
     * When received, it will set the pager to page 1, mode to JOIN_GROUP and set the code to the
     * queried one.
     * */
    private fun handleDeepLinkIntent(intent: Intent) {
        val appLinkAction = intent.action
        val appLinkData: Uri? = intent.data
        if (Intent.ACTION_VIEW == appLinkAction) {
            appLinkData?.lastPathSegment?.also { groupId ->
                viewModel.setPagerPage(1)
                viewModel.setGroupMode(JOIN_GROUP)
                viewModel.setGroupCode(groupId)
            }
        }
    }

    /**
     * Sets up the callback for handling the back button of the device.
     * The behaviour is based on the [viewModel] to return to the previous ViewPager page
     * */
    private fun setUpOnBackCallback() {
        onBackCallback = onBackPressedDispatcher.addCallback(this) {
            viewModel.setPagerPage(viewModel.getCurrentPage() - 1)
        }
    }

    /**
     * Helper function to actually remove the callback and let the back button function as normally
     *
     * @param callback: actual callback to be removed
     * */
    private fun removeOnBackCallback(callback: OnBackPressedCallback?) = callback?.remove()
}