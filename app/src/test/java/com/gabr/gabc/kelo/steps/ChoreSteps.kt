package com.gabr.gabc.kelo.steps

import com.gabr.gabc.kelo.choreDetail.ChoreDetailFunctions
import com.gabr.gabc.kelo.dataModels.Chore
import com.gabr.gabc.kelo.dataModels.User
import com.gabr.gabc.kelo.utils.DatesSingleton
import com.gabr.gabc.kelo.utils.PermissionsSingleton
import io.cucumber.java8.En
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import java.util.*

/** Defines the Chore Unit Tests */
@Suppress("unused")
class ChoreSteps : En {
    private lateinit var choreName: String
    private var chore = Chore()
    private lateinit var selectedDate: Calendar

    private var validChoreName = false
    private var validChore = false

    private var user = User()

    init {
        /**************************
         *     Validate Chore     *
         **************************/
        Given("the user that fills up a chore without an assignee") {
            chore = Chore("", "Do the laundry")
        }
        When("the user tries to create the chore") {
            validChore = ChoreDetailFunctions.validateChore(chore)
        }
        Then("the user will not be able to create it") {
            assertFalse(validChore)
        }

        /*******************************
         *     Selected Chore Date     *
         *******************************/
        Given("the user wants to create a new chore") {}
        When("the user enters the add chore page") {}
        When("the user selects a new expiration date") {
            selectedDate = ChoreDetailFunctions.parseAndUpdateChoreWithSelectedDate(chore, 22, 7, 22)
        }
        Then("the user will see that the importance is set to Low by default") {
            assertTrue(chore.points == 10)
        }
        Then("the user will see that the expiration date is set to today by default") {
            val c = Calendar.getInstance()
            chore.expiration?.let {
                c.time = it
                val choreDate = DatesSingleton.parseCalendarToStringOnList(c)
                val today = DatesSingleton.parseCalendarToStringOnList(Calendar.getInstance())
                assertTrue(choreDate == today)
            }
        }
        Then("the user will see that the expiration date is set to the one selected") {
            assertTrue(chore.expiration == selectedDate.time)
        }

        /*******************************
         *     Validate Chore Name     *
         *******************************/
        Given("the user that enters a chore {string}") { choreName: String ->
            this.choreName = choreName
        }
        When("the user validates the chore name") {
            validChoreName = ChoreDetailFunctions.isChoreNameValid(choreName)
        }
        Then("the chore name length must be less or equal than 32") {
            assertTrue(validChoreName)
        }
        Then("the chore name length must be greater or equal than 5") {
            assertTrue(validChoreName)
        }
        Then("the chore name length must not be greater than 32") {
            assertFalse(validChoreName)
        }
        Then("the chore name length must not be less than 5") {
            assertFalse(validChoreName)
        }
        Then("the chore name must not be empty") {
            assertFalse(validChoreName)
        }
        Then("the chore name must only contain alphanumerical characters with spaces") {
            assertTrue(validChoreName)
        }
        Then("the chore name must not contain special characters") {
            assertFalse(validChoreName)
        }

        /***********************************
         *     Update Chore Permission     *
         ***********************************/
        Given("a user with id {string} that wants to update a chore") { uid: String ->
            user.id = uid
        }
        When("the chore creator is {string}") { creatorId: String ->
            chore.creator = creatorId
        }
        Then("the user is permitted to update it") {
            assertTrue(PermissionsSingleton.isUserChoreCreator(chore.creator!!, user.id)
                    || PermissionsSingleton.isUserAdmin(user))
        }
        Then("the user is not permitted to update it") {
            assertFalse(PermissionsSingleton.isUserChoreCreator(chore.creator!!, user.id)
                    || PermissionsSingleton.isUserAdmin(user))
        }

        When("the user is the admin of the group") {
            user.isAdmin = true
        }

        /************************************
         *     Removal Chore Permission     *
         ************************************/
        Given("a user with id {string} that wants to remove a chore") { uid: String ->
            user.id = uid
        }
        When("the chore is not the creator {string} of the chore nor the admin") { creator: String ->
            chore.creator = creator
        }
        Then("the user is permitted to remove it") {
            assertTrue(PermissionsSingleton.isUserAdmin(user)
                    || PermissionsSingleton.isUserChoreCreator(chore.creator!!, user.id))
        }
        Then("the user is not permitted to remove it") {
            assertFalse(PermissionsSingleton.isUserAdmin(user)
                    || PermissionsSingleton.isUserChoreCreator(chore.creator!!, user.id))
        }

        /************************************
        *     Complete Chore Permission     *
        ************************************/

        Given("a user with id {string} that wants to complete a chore") { uid: String ->
            user.id = uid
        }
        When("the chore creator is either the {string} or the assignee {string}") { creator: String, assignee: String ->
            chore.creator = creator
            chore.assignee = assignee
        }
        When("the chore is not the creator {string} of the chore, nor the assignee {string}, nor the admin") {
                creator: String, assignee: String ->
            chore.creator = creator
            chore.assignee = assignee
        }
        Then("the user is permitted to complete it") {
            assertTrue(PermissionsSingleton.isUserAdmin(user)
                    || PermissionsSingleton.isUserChoreCreatorOrAssignee(chore.creator!!, chore.assignee!!, uid = user.id))
        }
        Then("the user is not permitted to complete it") {
            assertFalse(PermissionsSingleton.isUserAdmin(user)
                    || PermissionsSingleton.isUserChoreCreatorOrAssignee(chore.creator!!, chore.assignee!!, uid = user.id))
        }
    }
}