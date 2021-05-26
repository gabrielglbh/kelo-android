package com.gabr.gabc.kelo.welcome

import com.gabr.gabc.kelo.constants.Constants

/** Singleton instance that holds functions relevant only for WelcomeActivity and fragments */
object WelcomePageFunctions {
    /**
     * Validates group code to join.
     *
     * @param code: code to be validated
     * @return true if the validation is good
     * */
    fun isGroupCodeValid(code: String): Boolean = code.length == 20 && code.trim().isNotEmpty()

    /**
     * Validates group name.
     *
     * @param name: name to be validated
     * @return true if the validation is good
     * */
    fun isGroupNameValid(name: String): Boolean = name.matches(Regex(Constants.GROUP_NAME_VALIDATOR)) && name.trim().isNotEmpty()

    /**
     * Validates user name.
     *
     * @param name: name to be validated
     * @return true if the validation is good
     * */
    fun isUserNameValid(name: String): Boolean = name.matches(Regex(Constants.NAME_VALIDATOR)) && name.trim().isNotEmpty()
}