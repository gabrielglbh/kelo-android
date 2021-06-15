package com.gabr.gabc.kelo.utils

import android.app.Activity
import android.content.Context
import com.gabr.gabc.kelo.constants.Constants

/** Singleton instance with helper functions for Shared Preferences useful to all code */
object SharedPreferences {
    var isFirstLaunched: Boolean = false
    var groupId: String = ""
    var userId: String = ""

    /**
     * Function that resets the SharedPreferences of the user when leaving the group, removing the group...
     * */
    fun resetPreferences() {
        userId = ""
        groupId = ""
    }

    /**
     * Function that checks whether both of the [groupId] and [userId] are set to a certain value
     *
     * @return boolean deciding if the values are not empty
     * */
    fun checkGroupIdAndUserIdAreSet() = groupId.trim().isNotEmpty() && userId.trim().isNotEmpty()

    /**
     * Function that verifies if the current user is currently being displayed in screen
     *
     * @param userId: current user id
     * @param savedUid: current saved user id in SharedPreferences
     * @return true if the userId matches the saved one in shared preferences
     * */
    fun isUserBeingDisplayedCurrentUser(userId: String, savedUid: String? = this.userId): Boolean = userId == savedUid

    /**
     * Function that retrieves the Boolean pair of value-key by an specified key to the desired single instance
     * variable
     *
     * Called in SplashScreen ONLY, and then to retrieve call on respective variable
     *
     * @param activity: current activity
     * @param key: key to be retrieved
     * */
    fun getBooleanCode(activity: Activity, key: String) {
        val sharedPref = activity.getSharedPreferences(Constants.FIRST_LAUNCHED, Context.MODE_PRIVATE)
        val value = sharedPref.getBoolean(key, false)
        if (key == Constants.FIRST_LAUNCHED) isFirstLaunched = value
    }

    /**
     * Function that retrieves the String pair of value-key by an specified key to the desired single instance
     * variable
     *
     * Called in MainActivity ONLY, and then to retrieve call on respective variable
     *
     * @param activity: current activity
     * @param key: key to be retrieved
     * */
    fun getStringCode(activity: Activity, key: String) {
        val sharedPref = activity.getSharedPreferences(key, Context.MODE_PRIVATE)
        sharedPref.getString(key, "")?.let { if (key == Constants.GROUP_ID) groupId = it else userId = it }
    }

    /**
     * Function that writes a Boolean pair of value-key for the first launched activities of WelcomeFragment
     *
     * @param activity: current activity
     * @param key: key to be saved
     * @param code: code to be saved
     * */
    fun putBooleanCode(activity: Activity, key: String, code: Boolean) {
        val sharedPref = activity.getSharedPreferences(key, Context.MODE_PRIVATE) ?: return
        with (sharedPref.edit()) {
            putBoolean(key, code)
            commit()
        }
    }

    /**
     * Function that writes a String pair of value-key for the first launched activities of WelcomeFragment
     *
     * @param activity: current activity
     * @param key: key to be saved
     * @param code: code to be saved
     * */
    fun putStringCode(activity: Activity, key: String, code: String) {
        val sharedPref = activity.getSharedPreferences(key, Context.MODE_PRIVATE) ?: return
        with (sharedPref.edit()) {
            putString(key, code)
            commit()
        }
    }
}