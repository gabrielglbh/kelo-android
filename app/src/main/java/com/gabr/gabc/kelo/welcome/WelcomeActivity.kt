package com.gabr.gabc.kelo.welcome

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
import com.gabr.gabc.kelo.R
import com.gabr.gabc.kelo.constants.Constants
import com.gabr.gabc.kelo.utils.LoadingSingleton
import com.gabr.gabc.kelo.utils.UtilsSingleton
import com.gabr.gabc.kelo.welcome.viewPager.ViewPagerPage1
import com.gabr.gabc.kelo.welcome.viewPager.ViewPagerPage2
import com.gabr.gabc.kelo.welcome.viewPager.ViewPagerPage3

/** Activity that manages the ViewPager fragments and the entire log in logic for joining or creating a group */
class WelcomeActivity : AppCompatActivity() {

    private lateinit var parent: ConstraintLayout
    private lateinit var viewPager: ViewPager2
    private lateinit var backButton: View
    private lateinit var fullViewLoading: ConstraintLayout
    private lateinit var loading: ProgressBar

    private var onBackCallback: OnBackPressedCallback? = null

    private lateinit var viewModel: WelcomeViewModel

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        viewModel = run { ViewModelProvider(this).get(WelcomeViewModel::class.java) }
        UtilsSingleton.changeStatusBarColor(this, this, R.color.contrast)

        parent = findViewById(R.id.welcomePageConstraintLayout)
        parent.setOnClickListener { UtilsSingleton.hideKeyboard(this, parent) }

        backButton = findViewById(R.id.welcomeBackButton)
        backButton.setOnClickListener { viewModel.setPagerPage(viewModel.viewPagerPage.value!! - 1) }

        fullViewLoading = findViewById(R.id.loadingFragment)
        loading = findViewById(R.id.loadingWidget)

        setUpViewPager()
        setUpObserverLiveData()
        handleDeepLinkIntent(intent)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent?.let { handleDeepLinkIntent(intent) }
    }

    private fun setUpViewPager() {
        viewPager = findViewById(R.id.welcomePager)
        viewPager.adapter = ScreenSlidePagerAdapter(this)
        viewPager.isUserInputEnabled = false
    }

    private fun setUpObserverLiveData() {
        viewModel.viewPagerPage.observe(this, { mode ->
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
            LoadingSingleton.showFullLoadingScreen(this, parent, this.loading, fullViewLoading, loading)
        })
    }

    private fun handleDeepLinkIntent(intent: Intent) {
        val appLinkAction = intent.action
        val appLinkData: Uri? = intent.data
        if (Intent.ACTION_VIEW == appLinkAction) {
            appLinkData?.lastPathSegment?.also { groupId ->
                viewModel.setPagerPage(1)
                viewModel.setGroupMode(Constants.JOIN_GROUP)
                viewModel.setGroupCode(groupId)
            }
        }
    }

    private fun setUpOnBackCallback() {
        onBackCallback = onBackPressedDispatcher.addCallback(this) {
            viewModel.setPagerPage(viewModel.viewPagerPage.value!! - 1)
        }
    }

    private fun removeOnBackCallback(callback: OnBackPressedCallback?) = callback?.remove()
}