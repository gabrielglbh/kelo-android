package com.gabr.gabc.kelo.steps

import com.gabr.gabc.kelo.choreDetail.ChoreDetailFunctions
import com.gabr.gabc.kelo.models.Chore
import com.gabr.gabc.kelo.utils.DatesSingleton
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

    init {
        Given("the user that fills up a chore without an assignee") {
            chore = Chore("", "Do the laundry")
        }
        When("the user tries to create the chore") {
            validChore = ChoreDetailFunctions.validateChore(chore)
        }
        Then("the user will not be able to create it") {
            assertFalse(validChore)
        }

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
    }
}