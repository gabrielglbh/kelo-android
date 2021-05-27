package com.gabr.gabc.kelo.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ProgressBar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.gabr.gabc.kelo.R
import com.gabr.gabc.kelo.firebase.GroupQueries
import com.gabr.gabc.kelo.utils.LoadingSingleton
import com.gabr.gabc.kelo.utils.SharedPreferences
import com.gabr.gabc.kelo.utils.UtilsSingleton
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

    private lateinit var viewModel: LoadViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = run { ViewModelProvider(this).get(LoadViewModel::class.java) }

        parent = findViewById(R.id.mainActivityRoot)

        fullViewLoading = findViewById(R.id.loadingFragment)
        loading = findViewById(R.id.loadingWidget)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.mainNavHostFragment) as NavHostFragment
        navController = navHostFragment.navController

        bottomNavigation = findViewById(R.id.mainBottomNavigationView)

        setUpToolbar()
        setUpObserverForShowingLoadingScreen()
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
            else -> true
        }
    }

    private fun setUpToolbar() {
        UtilsSingleton.changeStatusBarColor(this, this, R.color.toolbarBackground)
        toolbar = findViewById(R.id.toolbar_widget)
        setSupportActionBar(toolbar)
        CoroutineScope(Dispatchers.Main).launch {
            SharedPreferences.groupId?.let { gid ->
                val group = GroupQueries().getGroup(gid)
                group?.let { gr -> supportActionBar?.subtitle = gr.name }
            }
        }
    }

    private fun setUpObserverForShowingLoadingScreen() {
        viewModel.isLoading.observe(this, { loading ->
            LoadingSingleton.showFullLoadingScreen(this, parent, this.loading, fullViewLoading, loading)
        })
    }

    private fun manageClickOnBottomNavigation() {
        bottomNavigation.setOnNavigationItemSelectedListener {
            val currentItem = bottomNavigation.menu.findItem(bottomNavigation.selectedItemId)
            when (it.itemId) {
                R.id.chores_menu -> {
                    supportActionBar?.title = getString(R.string.chores)
                    if (currentItem != it) navController.navigate(R.id.action_settings_to_choreList)
                    true
                }
                R.id.settings_menu -> {
                    supportActionBar?.title = getString(R.string.settings)
                    if (currentItem != it) navController.navigate(R.id.action_choreList_to_settings)
                    true
                }
                else -> false
            }
        }
    }
}