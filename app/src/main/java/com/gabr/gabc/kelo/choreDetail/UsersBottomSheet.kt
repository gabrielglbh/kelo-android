package com.gabr.gabc.kelo.choreDetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gabr.gabc.kelo.R
import com.gabr.gabc.kelo.firebase.UserQueries
import com.gabr.gabc.kelo.models.User
import com.gabr.gabc.kelo.utils.LoadingSingleton
import com.gabr.gabc.kelo.utils.SharedPreferences
import com.gabr.gabc.kelo.utils.UtilsSingleton
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/** Bottom Sheet Dialog Fragment that holds the user list for the Assignee button in ChoreDetailActivity */
class UsersBottomSheet : BottomSheetDialogFragment() {
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
            SharedPreferences.groupId?.let {
                CoroutineScope(Dispatchers.Main).launch {
                    val user = UserQueries().getMostLazyUser(it)
                    user?.let { u ->
                        viewModel.setAssignee(u)
                        dismiss()
                    }
                }
            }
        }

        userLists = view.findViewById(R.id.usersList)
        userLists.layoutManager = LinearLayoutManager(context)

        SharedPreferences.groupId?.let { getAllUsers(it) }
    }

    private fun getAllUsers(groupId: String) {
        CoroutineScope(Dispatchers.Main).launch {
            LoadingSingleton.manageLoadingView(loading, null, true)
            val users = UserQueries().getAllUsers(groupId)
            if (users != null) {
                userLists.adapter = UserAdapter(users)
                selectRandomUser.setOnClickListener {
                    UserQueries().getRandomUser(users)?.let { rndUser ->
                        viewModel.setAssignee(rndUser)
                        dismiss()
                    }
                }
            } else {
                dismiss()
                Toast.makeText(context, R.string.err_loading_users, Toast.LENGTH_SHORT).show()
            }
            LoadingSingleton.manageLoadingView(loading, null, false)
        }
    }

    /**
     * Adapter for the Recycler View.
     * It creates the [UserItem] for every position and attaches a listener for each item
     *
     * @param users: list of users retrieved from Firebase
     * */
    private inner class UserAdapter(private val users: ArrayList<User?>) : RecyclerView.Adapter<UserAdapter.UserItem>() {
        inner class UserItem(inflater: LayoutInflater, parent: ViewGroup)
            : RecyclerView.ViewHolder(inflater.inflate(R.layout.users_bottom_sheet_item, parent, false)) {
            private val parent: ConstraintLayout = itemView.findViewById(R.id.usersTab)
            private val name: TextView = itemView.findViewById(R.id.userName)
            private val avatar: ImageView = itemView.findViewById(R.id.userAvatar)

            /**
             * Initializes the view of the [UserItem] at a given position
             *
             * @param position: position of the list to be initialized
             * */
            fun initializeView(position: Int) {
                users[position]?.id?.let { uid ->
                    if (SharedPreferences.isUserBeingDisplayedCurrentUser(uid)) {
                        context?.let { UtilsSingleton.setTextAndIconToYou(it, name, avatar) }
                    } else {
                        avatar.setImageDrawable(UtilsSingleton.createAvatar(users[position]?.name))
                        name.text = users[position]?.name
                    }
                }
                parent.setOnClickListener {
                    users[position]?.let {
                        viewModel.setAssignee(it)
                        dismiss()
                    }
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserItem {
            return UserItem(LayoutInflater.from(parent.context), parent)
        }

        override fun onBindViewHolder(holder: UserItem, position: Int) { holder.initializeView(position) }

        override fun getItemCount(): Int { return users.size }
    }
}