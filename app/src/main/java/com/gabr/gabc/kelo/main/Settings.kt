package com.gabr.gabc.kelo.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gabr.gabc.kelo.R
import com.gabr.gabc.kelo.firebase.GroupQueries
import com.gabr.gabc.kelo.firebase.UserQueries
import com.gabr.gabc.kelo.models.User
import com.gabr.gabc.kelo.utils.DialogSingleton
import com.gabr.gabc.kelo.utils.PermissionsSingleton
import com.gabr.gabc.kelo.utils.SharedPreferences
import com.gabr.gabc.kelo.utils.common.UserListSwipeController
import com.gabr.gabc.kelo.utils.common.UsersAdapter
import com.gabr.gabc.kelo.welcome.WelcomeActivity
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/** Fragment that manages all settings of Kelo */
class Settings : Fragment(), UsersAdapter.UserClickListener {
    private lateinit var points: TextView
    private lateinit var deleteGroupButton: MaterialButton
    private lateinit var leaveGroupButton: MaterialButton
    private lateinit var userList: RecyclerView
    private lateinit var loading: ProgressBar

    private lateinit var viewModel: LoadViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = run { ViewModelProvider(this).get(LoadViewModel::class.java) }

        points = view.findViewById(R.id.userPoints)
        CoroutineScope(Dispatchers.Main).launch {
            if (SharedPreferences.userId != null && SharedPreferences.groupId != null) {
                val user = UserQueries().getUser(SharedPreferences.userId!!, SharedPreferences.groupId!!)
                if (user != null) points.text = user.points.toString()
                else points.text = "0"
            }
        }

        setUpUserList(view)

        deleteGroupButton = view.findViewById(R.id.settingsRemoveGroupButton)
        deleteGroupButton.setOnClickListener {
            DialogSingleton.createCustomDialog(
                requireActivity(),
                getString(R.string.settings_delete_group_button),
                getString(R.string.settings_dialog_msg_delete_group),
                getString(R.string.settings_dialog_btn_delete_positive),
            ) { deleteGroup() }
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
    }

    override fun onUserClicked(user: User?) {
        Toast.makeText(requireContext(), "USER CLICKED", Toast.LENGTH_SHORT).show()
    }

    private fun setUpUserList(view: View) {
        loading = view.findViewById(R.id.loadingWidget)
        userList = view.findViewById(R.id.settingsUserList)
        userList.layoutManager = LinearLayoutManager(requireContext())

        SharedPreferences.groupId?.let {
            val adapter = UsersAdapter(this, requireContext(), loading, it)
            val swipeHelper = ItemTouchHelper(UserListSwipeController(0,
                ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT, adapter, requireContext())
            )
            userList.adapter = adapter
            swipeHelper.attachToRecyclerView(userList)
        }
    }

    private fun deleteGroup() {
        viewModel.setLoading(true)
        CoroutineScope(Dispatchers.Main).launch {
            SharedPreferences.groupId?.let {
                val success = GroupQueries().deleteGroup(it)
                viewModel.setLoading(false)
                if (success) {
                    startWelcomeActivityAndResetPreferences()
                } else {
                    Toast.makeText(requireContext(), getString(R.string.err_group_delete), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun leaveGroup() {
        viewModel.setLoading(true)
        CoroutineScope(Dispatchers.Main).launch {
            if (SharedPreferences.userId != null && SharedPreferences.groupId != null) {
                val q = UserQueries()
                val user = q.getUser(SharedPreferences.userId!!, SharedPreferences.groupId!!)

                if (PermissionsSingleton.isUserAdmin(user)) {
                    val success = q.deleteUser(SharedPreferences.userId!!, SharedPreferences.groupId!!)
                    if (success) {
                        val adminChangedSuccess = q.updateNewAdmin(SharedPreferences.groupId!!)
                        if (adminChangedSuccess) {
                            startWelcomeActivityAndResetPreferences()
                        } else {
                            Toast.makeText(requireContext(), getString(R.string.err_group_leave), Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(requireContext(), getString(R.string.err_group_leave), Toast.LENGTH_SHORT).show()
                    }
                } else {
                    val success = q.deleteUser(SharedPreferences.userId!!, SharedPreferences.groupId!!)
                    if (success) {
                        startWelcomeActivityAndResetPreferences()
                    } else {
                        Toast.makeText(requireContext(), getString(R.string.err_group_leave), Toast.LENGTH_SHORT).show()
                    }
                }
                viewModel.setLoading(false)
            }
        }
    }

    private fun startWelcomeActivityAndResetPreferences() {
        SharedPreferences.resetPreferences()
        val intent = Intent(requireContext(), WelcomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }
}