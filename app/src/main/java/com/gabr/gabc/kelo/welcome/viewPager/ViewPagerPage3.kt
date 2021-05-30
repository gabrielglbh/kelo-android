package com.gabr.gabc.kelo.welcome.viewPager

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.gabr.gabc.kelo.R
import com.gabr.gabc.kelo.constants.Constants
import com.gabr.gabc.kelo.firebase.UserQueries
import com.gabr.gabc.kelo.main.MainActivity
import com.gabr.gabc.kelo.models.User
import com.gabr.gabc.kelo.utils.SharedPreferences
import com.gabr.gabc.kelo.utils.UtilsSingleton
import com.gabr.gabc.kelo.welcome.WelcomeViewModel
import com.gabr.gabc.kelo.welcome.WelcomePageFunctions
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/** Fragment included in the WelcomeActivity that holds the user name edit text and final page before user creation */
class ViewPagerPage3: Fragment() {

    private lateinit var label: TextView
    private lateinit var finalize: MaterialButton
    private lateinit var nameEditText: TextInputEditText
    private lateinit var nameInputLayout: TextInputLayout
    private lateinit var page: ConstraintLayout
    private lateinit var parent: ConstraintLayout

    private lateinit var viewModel: WelcomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = activity?.run { ViewModelProvider(this).get(WelcomeViewModel::class.java) }!!
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.view_pager_page_3, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        parent = requireActivity().findViewById(R.id.welcomePageConstraintLayout)

        page = view.findViewById(R.id.constraintLayoutPage3)
        page.setOnClickListener { UtilsSingleton.hideKeyboard(activity, view) }

        label = view.findViewById(R.id.nameQuestionLabel)

        nameInputLayout = view.findViewById(R.id.nameInputLayout)

        nameEditText = view.findViewById(R.id.nameEditText)
        nameEditText.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) UtilsSingleton.hideKeyboard(activity, v)
        }
        nameEditText.doOnTextChanged { _, _, _, _ -> UtilsSingleton.clearErrorFromTextLayout(nameInputLayout) }

        finalize = view.findViewById(R.id.finalizeWelcomeForm)
        finalize.setOnClickListener { validate() }

        animateObjectsIn()
    }

    private fun animateObjectsIn() {
        UtilsSingleton.createObjectAnimator(label, 500, -1000f)
        UtilsSingleton.createObjectAnimator(nameInputLayout, 500, -1000f)
        UtilsSingleton.createObjectAnimator(finalize, 700, -1500f)
    }

    private fun validate() {
        viewModel.setUserName(nameEditText.text.toString())
        if (!WelcomePageFunctions.isUserNameValid(viewModel.userName.value!!)) nameInputLayout.error = getString(R.string.err_invalid_name)
        else joinGroup()
    }

    private fun joinGroup() {
        CoroutineScope(Dispatchers.Main).launch {
            viewModel.setLoading(true)
            UtilsSingleton.clearErrorFromTextLayout(nameInputLayout)

            val groupId = viewModel.groupCode.value!!
            val mode = viewModel.groupSelectedMode.value
            val user = User("", viewModel.userName.value!!, 0, mode == Constants.CREATE_GROUP)

            when (val userId = UserQueries().joinGroup(groupId, user)) {
                "-1" -> UtilsSingleton.showSnackBar(parent, getString(R.string.err_join_group))
                "-2" -> UtilsSingleton.showSnackBar(parent, getString(R.string.err_username_taken))
                "-3" -> UtilsSingleton.showSnackBar(parent, getString(R.string.err_group_does_not_exist))
                else -> {
                    val intent = Intent(requireActivity(), MainActivity::class.java)
                    SharedPreferences.putIsFirstLaunched(requireActivity(), true)
                    SharedPreferences.putStringCode(requireActivity(), Constants.GROUP_ID, groupId)
                    SharedPreferences.putStringCode(requireActivity(), Constants.USER_ID, userId)
                    SharedPreferences.getStringCode(requireActivity(), Constants.GROUP_ID)
                    SharedPreferences.getStringCode(requireActivity(), Constants.USER_ID)
                    startActivity(intent)
                }
            }
            viewModel.setLoading(false)
        }
    }
}