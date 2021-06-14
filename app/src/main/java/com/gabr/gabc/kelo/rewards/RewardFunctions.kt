package com.gabr.gabc.kelo.rewards

import com.gabr.gabc.kelo.constants.Constants
import com.gabr.gabc.kelo.dataModels.Reward

/** Object that creates functions usable by the Rewards Activity and such */
object RewardFunctions {
    /**
     * Function that assures that the reward is completed and ready to be updated
     *
     * @param reward: [Reward] to be inserted or updated
     * @return true if reward is wrong
     * */
    fun validateReward(reward: Reward): Boolean = reward.expiration == null && reward.frequency != 0

    /**
     * Validates reward name
     *
     * @param name: name to be validated
     * @return true if the validation is good
     * */
    fun isRewardNameValid(name: String): Boolean = name.matches(Regex(Constants.REWARD_NAME_VALIDATOR)) && name.trim().isNotEmpty()
}