package com.gabr.gabc.kelo.welcomeActivity

import com.gabr.gabc.kelo.constants.GROUP_NAME_VALIDATOR
import com.gabr.gabc.kelo.constants.NAME_VALIDATOR

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
    fun isGroupNameValid(name: String): Boolean = name.matches(Regex(GROUP_NAME_VALIDATOR)) && name.trim().isNotEmpty()

    /**
     * Validates user name.
     *
     * @param name: name to be validated
     * @return true if the validation is good
     * */
    fun isUserNameValid(name: String): Boolean = name.matches(Regex(NAME_VALIDATOR)) && name.trim().isNotEmpty()
}