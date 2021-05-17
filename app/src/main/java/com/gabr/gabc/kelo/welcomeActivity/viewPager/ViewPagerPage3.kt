package com.gabr.gabc.kelo.welcomeActivity.viewPager

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.gabr.gabc.kelo.MainActivity
import com.gabr.gabc.kelo.R
import com.gabr.gabc.kelo.constants.GROUP_ID
import com.gabr.gabc.kelo.constants.USER_ID
import com.gabr.gabc.kelo.firebase.UserQueries
import com.gabr.gabc.kelo.models.User
import com.gabr.gabc.kelo.utils.SharedPreferences
import com.gabr.gabc.kelo.utils.UtilsSingleton
import com.gabr.gabc.kelo.welcomeActivity.WelcomeViewModel
import com.gabr.gabc.kelo.welcomeActivity.WelcomePageFunctions
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ViewPagerPage3: Fragment() {

    private lateinit var label: TextView
    private lateinit var finalize: MaterialButton
    private lateinit var nameEditText: TextInputEditText
    private lateinit var nameInputLayout: TextInputLayout
    private lateinit var parent: ConstraintLayout

    private lateinit var viewModel: WelcomeViewModel

    /**
     * Method that only serves for initializing the [viewModel] in a general way for all fragments
     * */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = activity?.run { ViewModelProvider(this).get(WelcomeViewModel::class.java) }!!
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.view_pager_page_3, container, false)
    }

    /**
     * Initialize all views from the layout
     * [parent] serves for the purpose of hiding the keyboard when touched outside of any EditText
     * [nameEditText] handles the name of the new user
     * [nameInputLayout] handles the errors of the edit text
     * [finalize] handles the completion of the whole form
     *
     * @param view: current view
     * @param savedInstanceState: current bundle, if any
     * */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        parent = view.findViewById(R.id.constraintLayoutPage3)
        parent.setOnClickListener { UtilsSingleton.hideKeyboard(activity, view) }

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

    /**
     * Creates the animations and triggers them when called. It is called when changing the page
     * from WelcomeFragment (based on the ViewModel) and when the app starts
     * */
    private fun animateObjectsIn() {
        UtilsSingleton.createObjectAnimator(label, 500, -1000f)
        UtilsSingleton.createObjectAnimator(nameInputLayout, 500, -1000f)
        UtilsSingleton.createObjectAnimator(finalize, 700, -1500f)
    }

    /**
     * Validates the name of the new user. It launches the MainActivity with the desired Group ID
     * */
    private fun validate() {
        viewModel.setUserName(nameEditText.text.toString())
        if (!WelcomePageFunctions.isUserNameValid(viewModel.getUserName())) nameInputLayout.error = getString(R.string.err_invalid_name)
        else joinGroup()
    }

    /**
     * Joins to a group with the current username
     * */
    private fun joinGroup() {
        CoroutineScope(Dispatchers.Main).launch {
            viewModel.setLoading(true)
            UtilsSingleton.clearErrorFromTextLayout(nameInputLayout)

            val groupId = viewModel.getGroupCode()
            val user = User("", viewModel.getUserName(), 0)

            when (val userId = UserQueries().joinGroup(groupId, user)) {
                "-1" -> Toast.makeText(context, getString(R.string.err_join_group), Toast.LENGTH_SHORT).show()
                "-2" -> Toast.makeText(context, getString(R.string.err_username_taken), Toast.LENGTH_SHORT).show()
                "-3" -> Toast.makeText(context, getString(R.string.err_group_does_not_exist), Toast.LENGTH_SHORT).show()
                else -> {
                    val intent = Intent(requireActivity(), MainActivity::class.java)
                    SharedPreferences.putIsFirstLaunched(requireActivity(), true)
                    SharedPreferences.putStringCode(requireActivity(), GROUP_ID, groupId)
                    SharedPreferences.putStringCode(requireActivity(), USER_ID, userId)
                    startActivity(intent)
                }
            }
            viewModel.setLoading(false)
        }
    }
}