package com.gabr.gabc.kelo.steps

import com.gabr.gabc.kelo.welcome.WelcomePageFunctions
import com.gabr.gabc.kelo.utils.common.CurrencyModel
import io.cucumber.java8.En
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue

/** Defines the Group Unit Tests */
@Suppress("unused")
class GroupSteps : En {
    private lateinit var groupName: String
    private lateinit var userName: String
    private var selectedCurrency: CurrencyModel? = null

    private var validGroupName = true

    init {
        /**
         * Group Name Validation
         * */
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
    }
}