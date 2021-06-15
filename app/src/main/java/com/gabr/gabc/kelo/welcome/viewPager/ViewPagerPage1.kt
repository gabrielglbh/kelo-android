package com.gabr.gabc.kelo.welcome.viewPager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.gabr.gabc.kelo.R
import com.gabr.gabc.kelo.constants.Constants
import com.gabr.gabc.kelo.utils.UtilsSingleton
import com.gabr.gabc.kelo.viewModels.WelcomeViewModel

/** Fragment included in the WelcomeActivity that holds the selection of the groupSelectedMode (create or join group) */
class ViewPagerPage1 : Fragment() {

    private lateinit var createGroupButton: Button
    private lateinit var joinGroupButton: Button
    private lateinit var label: TextView

    private lateinit var viewModel: WelcomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = activity?.run { ViewModelProvider(this).get(WelcomeViewModel::class.java) }!!
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.view_pager_page_1, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        label = view.findViewById(R.id.welcomeChoiceLabel)

        createGroupButton = view.findViewById(R.id.createGroupButton)
        createGroupButton.setOnClickListener {
            viewModel.setPagerPage(1)
            viewModel.setGroupMode(Constants.CREATE_GROUP)
        }

        joinGroupButton = view.findViewById(R.id.joinGroupButton)
        joinGroupButton.setOnClickListener {
            viewModel.setPagerPage(1)
            viewModel.setGroupMode(Constants.JOIN_GROUP)
        }

        setUpObserverLiveData()
        animateObjectsIn()
    }

    private fun setUpObserverLiveData() {
        viewModel.viewPagerPage.observe(viewLifecycleOwner, { mode ->
            if (mode == 0) animateObjectsIn()
        })
    }

    private fun animateObjectsIn() {
        UtilsSingleton.createObjectAnimator(label, 500, -1000f)
        UtilsSingleton.createObjectAnimator(createGroupButton, 700, -1500f)
        UtilsSingleton.createObjectAnimator(joinGroupButton, 700, -1500f)
    }
}