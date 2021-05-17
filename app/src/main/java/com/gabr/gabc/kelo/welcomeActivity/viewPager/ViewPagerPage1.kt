package com.gabr.gabc.kelo.welcomeActivity.viewPager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.gabr.gabc.kelo.R
import com.gabr.gabc.kelo.constants.CREATE_GROUP
import com.gabr.gabc.kelo.constants.JOIN_GROUP
import com.gabr.gabc.kelo.utils.UtilsSingleton
import com.gabr.gabc.kelo.welcomeActivity.WelcomeViewModel

class ViewPagerPage1 : Fragment() {

    private lateinit var createGroupButton: Button
    private lateinit var joinGroupButton: Button
    private lateinit var label: TextView

    private lateinit var viewModel: WelcomeViewModel

    /**
     * Method that only serves for initializing the [viewModel] in a general way for all fragments
     * */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = activity?.run { ViewModelProvider(this).get(WelcomeViewModel::class.java) }!!
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.view_pager_page_1, container, false)
    }

    /**
     * Initialize all views from the layout
     * [createGroupButton] and [joinGroupButton] handle what to show next
     *
     * @param view: current view
     * @param savedInstanceState: current bundle, if any
     * */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        label = view.findViewById(R.id.welcomeChoiceLabel)

        createGroupButton = view.findViewById(R.id.createGroupButton)
        createGroupButton.setOnClickListener {
            viewModel.setPagerPage(1)
            viewModel.setGroupMode(CREATE_GROUP)
        }

        joinGroupButton = view.findViewById(R.id.joinGroupButton)
        joinGroupButton.setOnClickListener {
            viewModel.setPagerPage(1)
            viewModel.setGroupMode(JOIN_GROUP)
        }

        setUpObserverLiveData()
        animateObjectsIn()
    }

    /**
     * Sets up the observer for the [viewModel] in order to animate this fragment's UI based on the
     * change of pages in the ViewPager2
     * */
    private fun setUpObserverLiveData() {
        viewModel.viewPagerMode.observe(viewLifecycleOwner, { mode ->
            if (mode == 0) animateObjectsIn()
        })
    }

    /**
     * Creates the animations and triggers them when called. It is called when changing the page
     * from WelcomeFragment (based on the ViewModel) and when the app starts
     * */
    private fun animateObjectsIn() {
        UtilsSingleton.createObjectAnimator(label, 500, -1000f)
        UtilsSingleton.createObjectAnimator(createGroupButton, 700, -1500f)
        UtilsSingleton.createObjectAnimator(joinGroupButton, 700, -1500f)
    }
}