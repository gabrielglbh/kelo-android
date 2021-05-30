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
     * */
    fun checkGroupIdAndUserIdAreSet() = groupId.trim().isNotEmpty() && userId.trim().isNotEmpty()

    /**
     * Function that verifies if the current user is currently being displayed in screen
     *
     * @param userId: current user id
     * @return true if the userId matches the saved one in shared preferences
     * */
    fun isUserBeingDisplayedCurrentUser(userId: String): Boolean = userId == SharedPreferences.userId

    /**
     * Function that sets the pair of value-key by an specified key to the desired single instance
     * variable, in this case isFirstLaunched
     *
     * Called in SplashScreen ONLY, and then to retrieve call: [isFirstLaunched]
     *
     * @param activity: current activity
     * */
    fun getIfFirstLaunched(activity: Activity) {
        val sharedPref = activity.getSharedPreferences(Constants.FIRST_LAUNCHED, Context.MODE_PRIVATE)
        isFirstLaunched = sharedPref.getBoolean(Constants.FIRST_LAUNCHED, false)
    }

    /**
     * Function that retrieves the pair of value-key by an specified key to the desired single instance
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
     * Function that writes a pair of value-key for the first launched activities of WelcomeFragment
     *
     * @param activity: current activity
     * @param isFirstLaunched: boolean to see if the user has created or joined a group
     * */
    fun putIsFirstLaunched(activity: Activity, isFirstLaunched: Boolean) {
        val sharedPref = activity.getSharedPreferences(Constants.FIRST_LAUNCHED, Context.MODE_PRIVATE) ?: return
        with (sharedPref.edit()) {
            putBoolean(Constants.FIRST_LAUNCHED, isFirstLaunched)
            commit()
        }
    }

    /**
     * Function that writes a pair of value-key for the first launched activities of WelcomeFragment
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