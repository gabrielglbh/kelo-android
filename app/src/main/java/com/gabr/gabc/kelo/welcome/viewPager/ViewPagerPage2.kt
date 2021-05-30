package com.gabr.gabc.kelo.welcome.viewPager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.gabr.gabc.kelo.R
import com.gabr.gabc.kelo.constants.*
import com.gabr.gabc.kelo.firebase.GroupQueries
import com.gabr.gabc.kelo.models.Group
import com.gabr.gabc.kelo.utils.UtilsSingleton
import com.gabr.gabc.kelo.utils.common.CurrencyBottomSheet
import com.gabr.gabc.kelo.utils.common.CurrencyModel
import com.gabr.gabc.kelo.welcome.WelcomeViewModel
import com.gabr.gabc.kelo.welcome.WelcomePageFunctions
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/** Fragment included in the WelcomeActivity that holds the group creation and join group views */
class ViewPagerPage2 : Fragment() {

    private lateinit var continueButton: Button
    private lateinit var groupNameLayout: TextInputLayout
    private lateinit var groupNameEditText: TextInputEditText
    private lateinit var groupCurrencyButton: Button
    private lateinit var groupCurrencyLabelTextView: TextView
    private lateinit var groupCurrencyTextView: TextView
    private lateinit var groupCurrencyImageView: ImageView
    private lateinit var joinGroupLayout: TextInputLayout
    private lateinit var joinGroupEditText: TextInputEditText
    private lateinit var parent: ConstraintLayout
    private lateinit var label: TextView

    private lateinit var currency: CurrencyModel
    private lateinit var viewModel: WelcomeViewModel

    private var mode = Constants.CREATE_GROUP

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = activity?.run { ViewModelProvider(this).get(WelcomeViewModel::class.java) }!!
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.view_pager_page_2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        parent = view.findViewById(R.id.constraintLayoutPage2)
        parent.setOnClickListener { UtilsSingleton.hideKeyboard(activity, view) }
        label = view.findViewById(R.id.createOrJoinGroupLabel)

        continueButton = view.findViewById(R.id.welcomeContinueButton)
        continueButton.setOnClickListener {
            if (mode == Constants.CREATE_GROUP) createGroup()
            else joinGroup()
        }

        currency = viewModel.groupCurrency.value!!
        groupCurrencyLabelTextView = view.findViewById(R.id.selectCurrencyLabel)
        groupCurrencyTextView = view.findViewById(R.id.selectedCurrency)
        groupCurrencyImageView = view.findViewById(R.id.selectedCurrencyFlag)
        groupCurrencyButton = view.findViewById(R.id.currencyButton)
        groupNameLayout = view.findViewById(R.id.groupNameInputLayout)
        groupNameEditText = view.findViewById(R.id.groupNameEditText)

        setCurrency(currency)

        joinGroupLayout = view.findViewById(R.id.joinGroupInputLayout)
        joinGroupEditText = view.findViewById(R.id.joinGroupEditText)

        setUpObserverLiveData()
        if (mode == Constants.CREATE_GROUP) {
            initViewsForGroupCreation()
            animateObjectsInForGroupCreation()
        } else {
            initViewsForJoiningGroup()
            animateObjectsInForJoiningGroup()
        }
    }

    private fun initViewsForGroupCreation() {
        groupCurrencyButton.setOnClickListener {
            CurrencyBottomSheet(welcomeViewModel = viewModel).show(childFragmentManager, CurrencyBottomSheet.TAG)
        }
        groupNameEditText.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) UtilsSingleton.hideKeyboard(activity, v)
        }
        groupNameEditText.doOnTextChanged { _, _, _, _ -> UtilsSingleton.clearErrorFromTextLayout(groupNameLayout) }
    }

    private fun initViewsForJoiningGroup() {
        joinGroupEditText.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) UtilsSingleton.hideKeyboard(activity, v)
        }
        joinGroupEditText.doOnTextChanged { _, _, _, _ -> UtilsSingleton.clearErrorFromTextLayout(joinGroupLayout) }
    }

    private fun createGroup() {
        viewModel.setGroupName(groupNameEditText.text.toString())
        if (!WelcomePageFunctions.isGroupNameValid(viewModel.groupName.value!!)) groupNameLayout.error = getString(R.string.err_invalid_name)
        else {
            CoroutineScope(Dispatchers.Main).launch {
                UtilsSingleton.clearErrorFromTextLayout(groupNameLayout)
                viewModel.setLoading(true)
                val group = Group("", viewModel.groupName.value!!, viewModel.groupCurrency.value!!.code)
                val result = GroupQueries().createGroup(group)
                if (result != null) {
                    viewModel.setGroupCode(result.id)
                    viewModel.setPagerPage(2)
                } else Toast.makeText(requireContext(), R.string.err_group_creation, Toast.LENGTH_SHORT).show()
                viewModel.setLoading(false)
            }
        }
    }

    private fun joinGroup() {
        viewModel.setGroupCode(joinGroupEditText.text.toString())
        if (!WelcomePageFunctions.isGroupCodeValid(viewModel.groupCode.value!!)) joinGroupLayout.error = getString(R.string.err_invalid_code)
        else {
            CoroutineScope(Dispatchers.Main).launch {
                viewModel.setLoading(true)
                UtilsSingleton.clearErrorFromTextLayout(joinGroupLayout)
                when (GroupQueries().checkGroupAvailability(viewModel.groupCode.value!!)) {
                    0 -> viewModel.setPagerPage(2)
                    -1 -> Toast.makeText(context, getString(R.string.err_join_group), Toast.LENGTH_SHORT).show()
                    -2 -> Toast.makeText(context, getString(R.string.err_group_full), Toast.LENGTH_SHORT).show()
                    -3 -> Toast.makeText(context, getString(R.string.err_group_does_not_exist), Toast.LENGTH_SHORT).show()
                }
                viewModel.setLoading(false)
            }
        }
    }

    private fun setUpObserverLiveData() {
        viewModel.groupCurrency.observe(viewLifecycleOwner, { currency -> setCurrency(currency) })

        viewModel.groupCode.observe(viewLifecycleOwner, { code ->
            joinGroupEditText.setText(code)
        })

        viewModel.groupSelectedMode.observe(viewLifecycleOwner, { mode ->
            if (mode == Constants.CREATE_GROUP) {
                animateObjectsInForGroupCreation()
                this.mode = mode
                label.text = getString(R.string.create_new_group_label)
                joinGroupLayout.visibility = View.GONE
                groupNameLayout.visibility = View.VISIBLE
                groupCurrencyButton.visibility = View.VISIBLE
                groupCurrencyLabelTextView.visibility = View.VISIBLE
                groupCurrencyTextView.visibility = View.VISIBLE
                groupCurrencyImageView.visibility = View.VISIBLE
            } else if (mode == Constants.JOIN_GROUP) {
                animateObjectsInForJoiningGroup()
                this.mode = mode
                label.text = getString(R.string.join_group_label)
                joinGroupLayout.visibility = View.VISIBLE
                groupNameLayout.visibility = View.GONE
                groupCurrencyLabelTextView.visibility = View.INVISIBLE
                groupCurrencyButton.visibility = View.INVISIBLE
                groupCurrencyTextView.visibility = View.INVISIBLE
                groupCurrencyImageView.visibility = View.INVISIBLE
            }
        })
    }

    private fun setCurrency(currency: CurrencyModel) {
        this.currency = currency
        groupCurrencyTextView.text = this.currency.code
        groupCurrencyImageView.setImageResource(this.currency.flag)
    }

    private fun animateObjectsInForGroupCreation() {
        UtilsSingleton.createObjectAnimator(label, 500, 1000f)
        UtilsSingleton.createObjectAnimator(continueButton, 700, 1500f)
        UtilsSingleton.createObjectAnimator(groupNameLayout, 500, 1000f)
        UtilsSingleton.createObjectAnimator(groupCurrencyLabelTextView, 500, 1000f)
        UtilsSingleton.createObjectAnimator(groupCurrencyButton, 500, 1000f)
        UtilsSingleton.createObjectAnimator(groupCurrencyTextView, 500, 1000f)
        UtilsSingleton.createObjectAnimator(groupCurrencyImageView, 500, 1000f)
    }

    private fun animateObjectsInForJoiningGroup() {
        UtilsSingleton.createObjectAnimator(label, 500, 1000f)
        UtilsSingleton.createObjectAnimator(continueButton, 700, 1500f)
        UtilsSingleton.createObjectAnimator(joinGroupLayout, 500, 1000f)
    }
}