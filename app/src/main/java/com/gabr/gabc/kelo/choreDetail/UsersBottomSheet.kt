package com.gabr.gabc.kelo.choreDetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gabr.gabc.kelo.R
import com.gabr.gabc.kelo.firebase.UserQueries
import com.gabr.gabc.kelo.models.User
import com.gabr.gabc.kelo.utils.SharedPreferences
import com.gabr.gabc.kelo.utils.UtilsSingleton
import com.gabr.gabc.kelo.utils.common.UsersAdapter
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/** Bottom Sheet Dialog Fragment that holds the user list for the Assignee button in ChoreDetailActivity */
class UsersBottomSheet : BottomSheetDialogFragment(), UsersAdapter.UserClickListener {
    companion object {
        const val TAG = "users_bottom_sheet"
    }

    private lateinit var viewModel: AssigneeViewModel
    private lateinit var userLists: RecyclerView
    private lateinit var loading: ProgressBar
    private lateinit var selectRandomUser: MaterialButton
    private lateinit var selectLazyUser: MaterialButton

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.users_bottom_sheet, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = activity?.run { ViewModelProvider(this).get(AssigneeViewModel::class.java) }!!
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        loading = view.findViewById(R.id.loadingWidget)

        selectRandomUser = view.findViewById(R.id.userAssigneeRandom)
        selectLazyUser = view.findViewById(R.id.userAssigneeLazy)
        selectLazyUser.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                val user = UserQueries().getMostLazyUser(SharedPreferences.groupId)
                user?.let { u ->
                    viewModel.setAssignee(u)
                    dismiss()
                }
            }
        }

        userLists = view.findViewById(R.id.usersList)
        userLists.layoutManager = LinearLayoutManager(context)

        getAllUsers()
    }

    private fun getAllUsers() {
        val listener = this
        CoroutineScope(Dispatchers.Main).launch {
            val users = UserQueries().getAllUsers(SharedPreferences.groupId)
            if (users != null) {
                userLists.adapter = UsersAdapter(users, requireContext(), requireView(), clickListener = listener)
                selectRandomUser.setOnClickListener {
                    CoroutineScope(Dispatchers.Main).launch {
                        UserQueries().getRandomUser(SharedPreferences.groupId)?.let { rndUser ->
                            viewModel.setAssignee(rndUser)
                            dismiss()
                        }
                    }
                }
            } else {
                UtilsSingleton.showSnackBar(requireView(), getString(R.string.err_loading_users))
            }
        }
    }

    override fun onUserClicked(user: User?) {
        user?.let {
            viewModel.setAssignee(it)
            dismiss()
        }
    }
}