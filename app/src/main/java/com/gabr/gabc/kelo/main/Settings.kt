package com.gabr.gabc.kelo.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.gabr.gabc.kelo.R
import com.gabr.gabc.kelo.constants.Constants
import com.gabr.gabc.kelo.firebase.GroupQueries
import com.gabr.gabc.kelo.firebase.UserQueries
import com.gabr.gabc.kelo.dataModels.Group
import com.gabr.gabc.kelo.utils.*
import com.gabr.gabc.kelo.utils.common.CurrencyBottomSheet
import com.gabr.gabc.kelo.utils.common.CurrencyModel
import com.gabr.gabc.kelo.utils.common.UserListSwipeController
import com.gabr.gabc.kelo.utils.common.UsersAdapter
import com.gabr.gabc.kelo.viewModels.MainViewModel
import com.gabr.gabc.kelo.viewModels.UserListViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/** Fragment that manages all settings of Kelo */
class Settings : Fragment() {
    private lateinit var points: TextView
    private lateinit var deleteGroupButton: MaterialButton
    private lateinit var leaveGroupButton: MaterialButton
    private lateinit var updateGroupButton: MaterialButton
    private lateinit var updateUserButton: MaterialButton
    private lateinit var rewardsButton: MaterialButton
    private lateinit var currencyGroupButton: MaterialButton
    private lateinit var userList: RecyclerView
    private lateinit var loading: ProgressBar
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var refresh: SwipeRefreshLayout

    private var group: Group? = null
    private lateinit var viewModel: MainViewModel
    private lateinit var userViewModel: UserListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = activity?.run { ViewModelProvider(this).get(MainViewModel::class.java) }!!
        userViewModel = activity?.run { ViewModelProvider(this).get(UserListViewModel::class.java) }!!
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bottomNavigationView = requireActivity().findViewById(R.id.mainBottomNavigationView)
        loading = view.findViewById(R.id.loadingWidget)
        points = view.findViewById(R.id.userPoints)

        updateGroupButton = view.findViewById(R.id.settingsUpdateGroupButton)
        updateGroupButton.setOnClickListener {
            DialogSingleton.createDialogWithEditTextField(
                requireActivity(),
                requireContext(),
                group = group,
                onSuccess = { newTitle ->
                    viewModel.setTitle(newTitle)
                    UtilsSingleton.showSnackBar(requireView(), getString(R.string.settings_successful_group_update),
                        anchorView = bottomNavigationView)
                }
            )
        }

        updateUserButton = view.findViewById(R.id.settingsUpdateUserButton)
        updateUserButton.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                val user = UserQueries().getUser(SharedPreferences.userId, SharedPreferences.groupId)
                if (user != null) {
                    DialogSingleton.createDialogWithEditTextField(
                        requireActivity(),
                        requireContext(),
                        user = user,
                        onSuccess = {
                            UtilsSingleton.showSnackBar(requireView(), getString(R.string.settings_successful_user_update),
                                anchorView = bottomNavigationView)
                        },
                    )
                }
            }
        }

        deleteGroupButton = view.findViewById(R.id.settingsRemoveGroupButton)
        deleteGroupButton.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                val user = UserQueries().getUser(SharedPreferences.userId, SharedPreferences.groupId)
                if (user?.isAdmin == true) {
                    DialogSingleton.createCustomDialog(
                        requireActivity(),
                        getString(R.string.settings_delete_group_button),
                        getString(R.string.settings_dialog_msg_delete_group),
                        getString(R.string.settings_dialog_btn_delete_positive),
                    ) { deleteGroup() }
                } else {
                    UtilsSingleton.showSnackBar(requireView(), getString(R.string.permission_remove_group),
                        anchorView = bottomNavigationView)
                }
            }
        }

        leaveGroupButton = view.findViewById(R.id.settingsExitGroupButton)
        leaveGroupButton.setOnClickListener {
            DialogSingleton.createCustomDialog(
                requireActivity(),
                getString(R.string.settings_leave_group_button),
                getString(R.string.settings_dialog_msg_leave_group),
                getString(R.string.settings_dialog_btn_leave_positive),
            ) { leaveGroup() }
        }

        rewardsButton = view.findViewById(R.id.settingsRewardsButton)
        rewardsButton.setOnClickListener {

        }

        viewModel.groupCurrency.observe(viewLifecycleOwner, { currency -> setCurrency(currency) })
        currencyGroupButton = view.findViewById(R.id.settingsCurrencyButton)
        currencyGroupButton.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                val user = UserQueries().getUser(SharedPreferences.userId, SharedPreferences.groupId)
                if (PermissionsSingleton.isUserAdmin(user)) {
                    CurrencyBottomSheet(mainViewModel = viewModel).show(childFragmentManager, CurrencyBottomSheet.TAG)
                } else {
                    UtilsSingleton.showSnackBar(requireView(), getString(R.string.permission_update_currency),
                        anchorView = bottomNavigationView)
                }
            }
        }

        refresh = view.findViewById(R.id.settingsRefresh)
        refresh.setOnRefreshListener {
            getUserPoints()
            getGroup()
            getUsers()
            refresh.isRefreshing = false
        }

        getUserPoints()
        getGroup()
        getUsers()

        userList = view.findViewById(R.id.settingsUserList)
        userList.layoutManager = LinearLayoutManager(requireContext())
        val adapter = UsersAdapter(this, userViewModel, requireContext(), requireView(),
            anchor = bottomNavigationView)
        val swipeHelper = ItemTouchHelper(UserListSwipeController(0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT, adapter, requireContext())
        )
        userList.adapter = adapter
        swipeHelper.attachToRecyclerView(userList)
    }

    private fun getUserPoints() {
        CoroutineScope(Dispatchers.Main).launch {
            if (SharedPreferences.checkGroupIdAndUserIdAreSet()) {
                val user = UserQueries().getUser(SharedPreferences.userId, SharedPreferences.groupId)
                if (user != null) points.text = user.points.toString()
                else points.text = "0"
            }
        }
    }

    private fun getGroup() {
        CoroutineScope(Dispatchers.Main).launch {
            group = GroupQueries().getGroup(SharedPreferences.groupId)
            group?.let { g ->
                val currentCurrency = Constants.CURRENCIES.filter { it.code == g.currency }[0]
                setCurrency(currentCurrency)
            }
        }
    }

    private fun getUsers() {
        LoadingSingleton.manageLoadingView(loading, null, true)
        CoroutineScope(Dispatchers.Main).launch {
            val users = UserQueries().getAllUsers(SharedPreferences.groupId)
            if (users != null) userViewModel.addAllUsers(users)
            else {
                UtilsSingleton.showSnackBar(requireView(), getString(R.string.err_loading_users),
                    anchorView = bottomNavigationView)
            }
            LoadingSingleton.manageLoadingView(loading, null, false)
        }
    }

    private fun setCurrency(currency: CurrencyModel) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                group?.let { gr ->
                    gr.currency = currency.code
                    val success = GroupQueries().updateGroup(gr)
                    if (success) {
                        currencyGroupButton.text = currency.code
                        val flag = ContextCompat.getDrawable(requireContext(), currency.flag)
                        currencyGroupButton.setCompoundDrawablesWithIntrinsicBounds(flag, null, null, null)
                    } else {
                        UtilsSingleton.showSnackBar(requireView(), getString(R.string.err_currency_update),
                            anchorView = bottomNavigationView)
                    }
                }
            } catch (e: IllegalStateException) {
                Log.e("CONTEXT ERROR", e.message.toString())
            }
        }
    }

    private fun deleteGroup() {
        viewModel.setLoading(true)
        CoroutineScope(Dispatchers.Main).launch {
            val success = GroupQueries().deleteGroup(SharedPreferences.groupId)
            viewModel.setLoading(false)
            if (success) {
                SharedPreferences.resetPreferences()
            } else {
                UtilsSingleton.showSnackBar(requireView(), getString(R.string.err_group_delete),
                    anchorView = bottomNavigationView)
            }
        }
    }

    private fun leaveGroup() {
        viewModel.setLoading(true)
        CoroutineScope(Dispatchers.Main).launch {
            if (SharedPreferences.checkGroupIdAndUserIdAreSet()) {
                val q = UserQueries()
                val gid = SharedPreferences.groupId
                val uid = SharedPreferences.userId
                val user = q.getUser(uid, gid)

                if (PermissionsSingleton.isUserAdmin(user)) {
                    val adminChangedSuccess = q.updateNewAdmin(gid)
                    if (adminChangedSuccess) {
                        val success = q.deleteUser(uid, gid)
                        if (success) {
                            SharedPreferences.resetPreferences()
                        } else {
                            UtilsSingleton.showSnackBar(requireView(), getString(R.string.err_group_leave),
                                anchorView = bottomNavigationView)
                        }
                    } else {
                        UtilsSingleton.showSnackBar(requireView(), getString(R.string.err_group_leave),
                            anchorView = bottomNavigationView)
                    }
                } else {
                    val success = q.deleteUser(uid, gid)
                    if (success) {
                        SharedPreferences.resetPreferences()
                    } else {
                        UtilsSingleton.showSnackBar(requireView(), getString(R.string.err_group_leave),
                            anchorView = bottomNavigationView)
                    }
                }
                viewModel.setLoading(false)
            }
        }
    }
}