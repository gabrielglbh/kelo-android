package com.gabr.gabc.kelo.utils

import android.content.Context
import android.content.Intent
import com.gabr.gabc.kelo.firebase.UserQueries
import com.gabr.gabc.kelo.models.User
import com.gabr.gabc.kelo.welcome.WelcomeActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/** Singleton instance with helper functions to validate the permission throughout the code */
object PermissionsSingleton {
    /**
     * Function that verifies if the current user is the admin of the group
     *
     * @param user: current user
     * @return boolean determining if the user is the admin
     * */
    fun isUserAdmin(user: User?) = user?.isAdmin ?: false

    /**
     * Attaches the attachListenerToAppForUserRemoved in the first seen view and when the user
     * is removed, navigates the user to the [WelcomeActivity]
     *
     * @param context: current context
     * */
    fun setListenerToUserRemoved(context: Context) {
        CoroutineScope(Dispatchers.Main).launch {
            UserQueries().attachListenerToAppForUserRemoved(SharedPreferences.groupId, SharedPreferences.userId) {
                SharedPreferences.resetPreferences()
                val intent = Intent(context, WelcomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                context.startActivity(intent)
            }
        }
    }

    /**
     * Function that verifies if the current user is the creator of a certain Chore
     *
     * @param choreCreator: UID creator of the chore
     * @return boolean determining if the user is the chore creator
     * */
    fun isUserChoreCreator(choreCreator: String) = choreCreator == SharedPreferences.userId

    /**
     * Function that verifies if the current user is the creator of a certain Chore or the assignee
     *
     * @param choreCreator: UID creator of the chore
     * @param assignee: UID assignee of the chore
     * @return boolean determining if the user is the chore creator
     * */
    fun isUserChoreCreatorOrAssignee(choreCreator: String, assignee: String) =
        choreCreator == SharedPreferences.userId || assignee == SharedPreferences.userId
}