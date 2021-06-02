package com.gabr.gabc.kelo.steps

import com.gabr.gabc.kelo.models.User
import com.gabr.gabc.kelo.utils.PermissionsSingleton
import com.gabr.gabc.kelo.utils.SharedPreferences
import com.gabr.gabc.kelo.welcome.WelcomePageFunctions
import io.cucumber.java8.En
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue

/** Defines the User Unit Tests */
@Suppress("unused")
class UserSteps : En {
    private lateinit var username: String
    private var valid = true

    private var user = User()
    private var userToBeRemoved = User()

    init {
        /******************************
         *     DELETE USER ACTION     *
         ******************************/
        Given("the user {string}") { uid: String ->
            user.id = uid
        }
        Given("the user whose {string} who is not the admin of the group") { uid: String ->
            user.id = uid
            user.isAdmin = false
        }
        Given("the user {string} who is admin of the group") { uid: String ->
            user.id = uid
            user.isAdmin = true
        }
        When("the user tries to remove a certain user {string}") { removed: String ->
            userToBeRemoved.id = removed
        }
        Then("the action of removal will not be executed") {
            val isNotTheSame = SharedPreferences.isUserBeingDisplayedCurrentUser(user.id, userToBeRemoved.id)
            val isAdmin = PermissionsSingleton.isUserAdmin(user)
            assertFalse(!isNotTheSame && isAdmin)
        }
        Then("the action of removal will be executed") {
            val isNotTheSame = SharedPreferences.isUserBeingDisplayedCurrentUser(user.id, userToBeRemoved.id)
            val isAdmin = PermissionsSingleton.isUserAdmin(user)
            assertTrue(!isNotTheSame && isAdmin)
        }

        /******************************
         *     USER NAME VALIDATION   *
         ******************************/
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