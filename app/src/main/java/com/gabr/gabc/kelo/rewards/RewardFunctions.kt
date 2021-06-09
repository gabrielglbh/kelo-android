package com.gabr.gabc.kelo.rewards

import com.gabr.gabc.kelo.constants.Constants

/** Object that creates functions usable by the Rewards Activity and such */
object RewardFunctions {
    /**
     * Validates reward name
     *
     * @param name: name to be validated
     * @return true if the validation is good
     * */
    fun isRewardNameValid(name: String): Boolean = name.matches(Regex(Constants.REWARD_NAME_VALIDATOR)) && name.trim().isNotEmpty()
}