package com.gabr.gabc.kelo.tutorial.viewPager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.gabr.gabc.kelo.R

/** Class defining the fourth page of the tutorial view pager */
class TutorialPager4 : Fragment()  {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.tutorial_pager_page_4, container, false)
    }
}