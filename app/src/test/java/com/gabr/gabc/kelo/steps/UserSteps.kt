package com.gabr.gabc.kelo.steps

import com.gabr.gabc.kelo.welcome.WelcomePageFunctions
import io.cucumber.java8.En
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue

/** Defines the User Unit Tests */
@Suppress("unused")
class UserSteps : En {
    private lateinit var username: String
    private var valid = true

    init {
        Given("the user that enters its username {string}") { username: String ->
           this.username = username
        }
        When("the user validates its username") {
            valid = WelcomePageFunctions.isUserNameValid(username)
        }
        Then("the username length must be less or equal than 32") {
            assertTrue(valid)
        }
        Then("the username length must be greater or equal than 3") {
            assertTrue(valid)
        }
        Then("the username length must not be greater than 32") {
            assertFalse(valid)
        }
        Then("the username length must not be less than 3") {
            assertFalse(valid)
        }
        Then("the username must not be empty") {
            assertFalse(valid)
        }
        Then("the username must only contain alphabetical characters with spaces") {
            assertTrue(valid)
        }
        Then("the username must not contain numbers") {
            assertFalse(valid)
        }
        Then("the username must not contain special characters") {
            assertFalse(valid)
        }
    }
}