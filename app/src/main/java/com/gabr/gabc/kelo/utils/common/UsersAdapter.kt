package com.gabr.gabc.kelo.utils.common

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.gabr.gabc.kelo.R
import com.gabr.gabc.kelo.firebase.UserQueries
import com.gabr.gabc.kelo.models.User
import com.gabr.gabc.kelo.utils.LoadingSingleton
import com.gabr.gabc.kelo.utils.PermissionsSingleton
import com.gabr.gabc.kelo.utils.SharedPreferences
import com.gabr.gabc.kelo.utils.UtilsSingleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Adapter for the Recycler View.
 * It creates the [UserItem] for every position and attaches a listener for each item
 *
 * @param context: context from the caller
 * @param loading: [ProgressBar] widget for showing it and hide it when loading
 * @param groupId: group id to retrieve the chores from
 * */
class UsersAdapter(private val listener: UserClickListener, private val context: Context,
                   loading: ProgressBar, groupId: String?)
    : RecyclerView.Adapter<UsersAdapter.UserItem>() {

    /**
     * Interface that defines a function to be called by the initializer when clicking on a certain item
     * */
    interface UserClickListener {
        /**
         * Function that gets the selected [User] in the adapter to be managed by the caller
         *
         * @param user: clicked [User]
         * */
        fun onUserClicked(user: User?)
    }
    var users: ArrayList<User> = arrayListOf()

    /**
     * Initializes the listener to the chores list in Firebase. It updates
     * the list every time the collection changes
     * */
    init {
        if (groupId != null) {
            LoadingSingleton.manageLoadingView(loading, null, true)
            val listener = UserQueries().attachListenerToUsers(groupId,
                { position, user -> addUserAtPosition(position, user) },
                { position, user -> updateUserAtPosition(position, user) },
                { position -> removeUserAtPosition(position) })

            if (listener == null) Toast.makeText(context, R.string.err_loading_users, Toast.LENGTH_SHORT).show()
            LoadingSingleton.manageLoadingView(loading, null, false)
        } else Toast.makeText(context, R.string.err_group_does_not_exist, Toast.LENGTH_SHORT).show()
    }

    private fun addUserAtPosition(position: Int, user: User) {
        users.add(position, user)
        notifyItemInserted(position)
    }

    private fun updateUserAtPosition(position: Int, user: User) {
        users[position] = user
        notifyItemChanged(position)
    }

    private fun removeUserAtPosition(position: Int) {
        users.removeAt(position)
        notifyItemRemoved(position)
    }

    /**
     * Removes a certain [User] of the list. If it fails somehow, [notifyItemChanged] is called
     * to make the view return to its initial position
     *
     * @param position: position in which to remove the item
     * */
    fun removeUserFromGroupOnSwap(position: Int) {
        users[position].id.let {
            SharedPreferences.groupId?.let { gid ->
                SharedPreferences.userId?.let { uid ->
                    removalRoutine(it, gid, uid)
                }
            }
        }
        notifyItemChanged(position)
    }

    private fun removalRoutine(actualId: String, gid: String, uid: String) {
        CoroutineScope(Dispatchers.Main).launch {
            val user = UserQueries().getUser(uid, gid)
            if (PermissionsSingleton.isUserAdmin(user)) {
                if (!SharedPreferences.isUserBeingDisplayedCurrentUser(actualId)) {
                    val success = UserQueries().deleteUser(actualId, gid)
                    if (!success) {
                        Toast.makeText(context, R.string.err_user_delete, Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(context, "You are not the admin of the group", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * Inner class that holds the layout for the items in the RecyclerView
     *
     * @param inflater: which layout to inflate on each item
     * */
    inner class UserItem(inflater: LayoutInflater, parent: ViewGroup) : RecyclerView.ViewHolder(
        inflater.inflate(R.layout.users_list_item, parent, false)), View.OnClickListener {
        private val parent: ConstraintLayout = itemView.findViewById(R.id.usersTab)
        private val name: TextView = itemView.findViewById(R.id.userName)
        private val isAdmin: TextView = itemView.findViewById(R.id.admin)
        private val points: TextView = itemView.findViewById(R.id.userPoints)
        private val avatar: ImageView = itemView.findViewById(R.id.userAvatar)

        override fun onClick(v: View?) { listener.onUserClicked(users[layoutPosition]) }

        /**
         * Initializes the view of the [UserItem] at a given position
         *
         * @param position: position of the list to be initialized
         * */
        fun initializeView(position: Int) {
            name.isSelected = true
            users[position].id.let { uid ->
                avatar.setImageDrawable(UtilsSingleton.createAvatar(users[position].name))
                if (SharedPreferences.isUserBeingDisplayedCurrentUser(uid)) {
                    name.text = UtilsSingleton.setTextForCurrentUser(context, users[position].name)
                }
                else name.text = users[position].name
            }

            if (PermissionsSingleton.isUserAdmin(users[position])) isAdmin.visibility = View.VISIBLE

            users[position].points.let { pts ->
                val p = "Pts: $pts"
                points.text = p
            }
            parent.setOnClickListener(this)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserItem {
        return UserItem(LayoutInflater.from(parent.context), parent)
    }

    override fun onBindViewHolder(holder: UserItem, position: Int) { holder.initializeView(position) }

    override fun getItemCount(): Int { return users.size }
}