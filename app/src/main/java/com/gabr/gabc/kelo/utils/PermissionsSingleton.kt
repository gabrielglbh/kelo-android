package com.gabr.gabc.kelo.utils

import com.gabr.gabc.kelo.models.User

/** Singleton instance with helper functions to validate the permission throughout the code */
object PermissionsSingleton {
    /**
     * Function that verifies if the current user is the admin of the group
     *
     * @param user: current user
     * @return boolean determining if the user is the admin
     * */
    fun isUserAdmin(user: User) = user.isAdmin

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
        choreCreator == SharedPreferences.userId && choreCreator == assignee
}