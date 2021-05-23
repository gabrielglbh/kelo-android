package com.gabr.gabc.kelo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.gabr.gabc.kelo.constants.GROUP_ID
import com.gabr.gabc.kelo.constants.USER_ID
import com.gabr.gabc.kelo.mainActivity.ShareCodeBottomSheet
import com.gabr.gabc.kelo.utils.SharedPreferences
import com.gabr.gabc.kelo.utils.UtilsSingleton
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView

/** Activity that manages the settings and chore list Fragments */
class MainActivity : AppCompatActivity() {

    private lateinit var toolbar: MaterialToolbar

    private lateinit var parent: ConstraintLayout
    private lateinit var bottomNavigation: BottomNavigationView
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        SharedPreferences.getStringCode(this, GROUP_ID)
        SharedPreferences.getStringCode(this, USER_ID)

        parent = findViewById(R.id.mainActivityRoot)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.mainNavHostFragment) as NavHostFragment
        navController = navHostFragment.navController

        bottomNavigation = findViewById(R.id.mainBottomNavigationView)

        setUpToolbar()
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