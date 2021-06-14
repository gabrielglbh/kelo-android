package com.gabr.gabc.kelo.steps

import com.gabr.gabc.kelo.dataModels.Reward
import com.gabr.gabc.kelo.dataModels.User
import com.gabr.gabc.kelo.rewards.RewardFunctions
import com.gabr.gabc.kelo.utils.PermissionsSingleton
import io.cucumber.java8.En
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import java.util.*

/** Defines the Reward Unit Tests */
@Suppress("unused")
class RewardSteps : En {
    private lateinit var description: String
    private var validRewardName = true
    private var validReward = false

    private var reward = Reward()
    private var user = User()

    init {
        /**********************************
         *        Validate Reward         *
         **********************************/
        Given("the user that fills up a invalid reward") {
            reward = Reward("", "reward name")
        }
        Given("the user that fills up a valid reward") {
            reward = Reward("", "reward name", Calendar.getInstance().time, frequency = 2)
        }
        When("the user tries to create the reward") {
            validReward = RewardFunctions.validateReward(reward)
        }
        Then("the user will not be able to create the reward") {
            assertTrue(validReward)
        }
        Then("the user will be able to create the reward") {
            assertFalse(validReward)
        }

        /***********************************
         * Reward Description Validation   *
         ***********************************/
        Given("the user that enters the reward description {string}") { description: String ->
            this.description = description
        }
        When("the user validates its reward") {
            validRewardName = RewardFunctions.isRewardNameValid(description)
        }
        Then("the reward description length must be less or equal than 48") {
            assertTrue(validRewardName)
        }
        Then("the reward description length must be greater or equal than 5") {
            assertTrue(validRewardName)
        }
        Then("the reward description length must not be greater than 48") {
            assertFalse(validRewardName)
        }
        Then("the reward description length must not be less than 5") {
            assertFalse(validRewardName)
        }
        Then("the reward description must not be empty") {
            assertFalse(validRewardName)
        }
        Then("the reward description must only contain alphanumerical characters with spaces") {
            assertTrue(validRewardName)
        }
        Then("the reward description must not contain special characters") {
            assertFalse(validRewardName)
        }

        /***********************************
         *     Create Reward Permission     *
         ***********************************/
        Given("a user that wants to create the reward") {}
        /***********************************
         *     Update Reward Permission     *
         ***********************************/
        Given("a user that wants to update the reward") {}

        /***********************************
         *     Remove Reward Permission     *
         ***********************************/
        Given("a user that wants to remove the reward") {}

        When("the user is not the admin of the group") {
            user.isAdmin = false
        }
        When("the user is the administrator of the group") {
            user.isAdmin = true
        }

        Then("the user is permitted to modify it") {
            assertTrue(PermissionsSingleton.isUserAdmin(user))
        }
        Then("the user is not permitted to modify it") {
            assertFalse(PermissionsSingleton.isUserAdmin(user))
        }
    }
}