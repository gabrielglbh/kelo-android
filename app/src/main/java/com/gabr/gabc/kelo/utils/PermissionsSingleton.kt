package com.gabr.gabc.kelo.utils

import com.gabr.gabc.kelo.dataModels.User

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
     * Function that verifies the current users in a group and determines if a new user should
     * be the admin of the group
     *
     * @param users: list of users
     * @return boolean determining if the user is set to be the admin of the group
     * */
    fun willUserBeAdmin(users: ArrayList<User>?) = users != null && users.size == 0

    /**
     * Function that verifies if the current user is the creator of a certain Chore
     *
     * @param choreCreator: UID creator of the chore
     * @param uid: By default the saved UID on [SharedPreferences]
     * @return boolean determining if the user is the chore creator
     * */
    fun isUserChoreCreator(choreCreator: String, uid: String? = SharedPreferences.userId) = choreCreator == uid

    /**
     * Function that verifies if the current user is the creator of a certain Chore or the assignee
     *
     * @param choreCreator: UID creator of the chore
     * @param assignee: UID assignee of the chore
     * @param uid: By default the saved UID on [SharedPreferences]
     * @return boolean determining if the user is the chore creator
     * */
    fun isUserChoreCreatorOrAssignee(choreCreator: String, assignee: String, uid: String? = SharedPreferences.userId) =
        choreCreator == uid || assignee == uid
}