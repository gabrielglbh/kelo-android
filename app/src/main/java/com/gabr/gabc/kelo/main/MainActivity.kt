package com.gabr.gabc.kelo.main

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ProgressBar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.gabr.gabc.kelo.R
import com.gabr.gabc.kelo.constants.Constants
import com.gabr.gabc.kelo.firebase.GroupQueries
import com.gabr.gabc.kelo.firebase.UserQueries
import com.gabr.gabc.kelo.utils.LoadingSingleton
import com.gabr.gabc.kelo.utils.SharedPreferences
import com.gabr.gabc.kelo.utils.UtilsSingleton
import com.gabr.gabc.kelo.viewModels.ChoreListViewModel
import com.gabr.gabc.kelo.viewModels.MainViewModel
import com.gabr.gabc.kelo.welcome.WelcomeActivity
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/** Activity that manages the settings and chore list Fragments */
class MainActivity : AppCompatActivity() {

    private lateinit var toolbar: MaterialToolbar

    private lateinit var parent: ConstraintLayout
    private lateinit var bottomNavigation: BottomNavigationView
    private lateinit var navController: NavController

    private lateinit var fullViewLoading: ConstraintLayout
    private lateinit var loading: ProgressBar

    private lateinit var viewModel: MainViewModel
    private lateinit var choreListViewModel: ChoreListViewModel

    private var showCompletedChores = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (!SharedPreferences.checkGroupIdAndUserIdAreSet()) {
            SharedPreferences.getStringCode(this, Constants.GROUP_ID)
            SharedPreferences.getStringCode(this, Constants.USER_ID)
        }

        updateFCMTokenIfNecessary()
        setListenerToUserRemoved(baseContext)

        viewModel = run { ViewModelProvider(this).get(MainViewModel::class.java) }
        choreListViewModel = run { ViewModelProvider(this).get(ChoreListViewModel::class.java) }

        parent = findViewById(R.id.mainActivityRoot)

        fullViewLoading = findViewById(R.id.loadingFragment)
        loading = findViewById(R.id.loadingWidget)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.mainNavHostFragment) as NavHostFragment
        navController = navHostFragment.navController

        bottomNavigation = findViewById(R.id.mainBottomNavigationView)

        setUpToolbar()
        setUpObserverLiveData()
        manageClickOnBottomNavigation()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        menu?.findItem(R.id.toolbar_done)?.isVisible = false
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.toolbar_share -> {
                ShareCodeBottomSheet().show(supportFragmentManager, ShareCodeBottomSheet.TAG)
                true
            }
            R.id.toolbar_completed_chores -> {
                showCompletedChores = !showCompletedChores
                choreListViewModel.setShowCompleted(showCompletedChores)

                if (showCompletedChores) item.icon = ContextCompat.getDrawable(this, R.drawable.ic_todo)
                else item.icon = ContextCompat.getDrawable(this, R.drawable.ic_completed)
                true
            }
            else -> true
        }
    }

    private fun setListenerToUserRemoved(context: Context) {
        CoroutineScope(Dispatchers.Main).launch {
            UserQueries().attachListenerToAppForUserRemoved(SharedPreferences.groupId, SharedPreferences.userId) {
                SharedPreferences.resetPreferences()
                val intent = Intent(context, WelcomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                val bundle = Bundle()
                bundle.putBoolean(WelcomeActivity.REMOVED_FROM_GROUP, true)
                context.startActivity(intent, bundle)
            }
        }
    }

    private fun updateFCMTokenIfNecessary() {
        CoroutineScope(Dispatchers.Main).launch { UserQueries().getFCMToken() }
    }

    private fun setUpToolbar() {
        UtilsSingleton.changeStatusBarColor(this, this, R.color.toolbarBackground)
        toolbar = findViewById(R.id.toolbar_widget)
        setSupportActionBar(toolbar)
        CoroutineScope(Dispatchers.Main).launch {
            val group = GroupQueries().getGroup(SharedPreferences.groupId)
            group?.let { gr -> supportActionBar?.subtitle = gr.name }
        }
    }

    private fun setUpObserverLiveData() {
        viewModel.isLoading.observe(this, { loading ->
            LoadingSingleton.showFullLoadingScreen(this, parent, this.loading, fullViewLoading, loading)
        })
        viewModel.groupName.observe(this, { supportActionBar?.subtitle = it })
        choreListViewModel.actionBarTitle.observe(this, { title -> supportActionBar?.title = title })
    }

    private fun manageClickOnBottomNavigation() {
        bottomNavigation.setOnNavigationItemSelectedListener {
            val currentItem = bottomNavigation.menu.findItem(bottomNavigation.selectedItemId)
            when (it.itemId) {
                R.id.chores_menu -> {
                    supportActionBar?.title = getString(R.string.chores)
                    toolbar.menu.findItem(R.id.toolbar_completed_chores).isVisible = true
                    if (currentItem != it) navController.navigate(R.id.action_settings_to_choreList)
                    true
                }
                R.id.settings_menu -> {
                    supportActionBar?.title = getString(R.string.settings)
                    toolbar.menu.findItem(R.id.toolbar_completed_chores).isVisible = false
                    if (currentItem != it) navController.navigate(R.id.action_choreList_to_settings)
                    true
                }
                else -> false
            }
        }
    }
}