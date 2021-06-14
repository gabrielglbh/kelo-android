package com.gabr.gabc.kelo.steps

import com.gabr.gabc.kelo.dataModels.User
import com.gabr.gabc.kelo.utils.PermissionsSingleton
import com.gabr.gabc.kelo.welcome.WelcomePageFunctions
import io.cucumber.java8.En
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue

/** Defines the Group Unit Tests */
@Suppress("unused")
class GroupSteps : En {
    private lateinit var groupName: String
    private var validGroupName = true

    private var user = User()

    init {
        /*************************
         * Group Name Validation *
         *************************/
        Given("the user that enters its group name {string}") { groupName: String ->
            this.groupName = groupName
        }
        When("the user validates its group name") {
            validGroupName = WelcomePageFunctions.isGroupNameValid(groupName)
        }
        Then("the group name length must be less or equal than 32") {
            assertTrue(validGroupName)
        }
        Then("the group name length must be greater or equal than 5") {
            assertTrue(validGroupName)
        }
        Then("the group name length must not be greater than 32") {
            assertFalse(validGroupName)
        }
        Then("the group name length must not be less than 5") {
            assertFalse(validGroupName)
        }
        Then("the group name must not be empty") {
            assertFalse(validGroupName)
        }
        Then("the group name must only contain alphanumerical characters with spaces") {
            assertTrue(validGroupName)
        }
        Then("the group name must not contain special characters") {
            assertFalse(validGroupName)
        }

        /**********************************
         *    Validate Update Group Name  *
         **********************************/
        Given("a user that wants to update the group name") {}
        When("the user is the unique administrator of the group") {
            user.isAdmin = true
        }
        When("the user is not the unique administrator of the group") {
            user.isAdmin = false
        }
        Then("the user is permitted to modify the group name") {
            assertTrue(PermissionsSingleton.isUserAdmin(user))
        }
        Then("the user is not permitted to modify the group name") {
            assertFalse(PermissionsSingleton.isUserAdmin(user))
        }
    }
}