package com.gabr.gabc.kelo.tutorial

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.gabr.gabc.kelo.R
import com.gabr.gabc.kelo.tutorial.viewPager.TutorialPager1
import com.gabr.gabc.kelo.tutorial.viewPager.TutorialPager2
import com.gabr.gabc.kelo.tutorial.viewPager.TutorialPager3
import com.gabr.gabc.kelo.tutorial.viewPager.TutorialPager4
import com.gabr.gabc.kelo.utils.UtilsSingleton

/** Class that holds the tutorial view pager */
class TutorialActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager2
    private lateinit var back: View

    /** Inner class that holds the creation of the view pager for the tutorial itself */
    private inner class TutorialSlidePager(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
        override fun getItemCount(): Int = 4
        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> TutorialPager1()
                1 -> TutorialPager2()
                2 -> TutorialPager3()
                3 -> TutorialPager4()
                else -> TutorialPager4()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tutorial)

        UtilsSingleton.changeStatusBarColor(this, this, R.color.contrast)

        back = findViewById(R.id.tutorialBackButton)
        back.setOnClickListener { finish() }

        viewPager = findViewById(R.id.tutorialPager)
        viewPager.adapter = TutorialSlidePager(this)
    }
}