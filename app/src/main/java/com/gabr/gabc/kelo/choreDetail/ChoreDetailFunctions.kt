package com.gabr.gabc.kelo.choreDetail

import com.gabr.gabc.kelo.constants.CHORE_NAME_VALIDATOR
import com.gabr.gabc.kelo.models.Chore
import com.gabr.gabc.kelo.utils.widgets.CustomDatePicker
import java.util.*

/** Singleton instance with helper functions only relevant for ChoreDetailActivity */
object ChoreDetailFunctions {
    /**
     * Function that assures that the chore is completed and ready to be updated
     *
     * @param chore: [Chore] to be inserted or updated
     * @return true if chore is ready
     * */
    fun validateChore(chore: Chore): Boolean = chore.assignee != "" && chore.name != ""

    /**
     * Validates chore name
     *
     * @param name: name to be validated
     * @return true if the validation is good
     * */
    fun isChoreNameValid(name: String): Boolean = name.matches(Regex(CHORE_NAME_VALIDATOR)) && name.trim().isNotEmpty()

    /**
     * Function that is called when a date is selected in the [CustomDatePicker]
     *
     * @param chore: current [Chore] to be updated
     * @param day: selected day
     * @param month: selected month
     * @param year: selected year
     * @return [Calendar] object containing the selected date
     * */
    fun parseAndUpdateChoreWithSelectedDate(chore: Chore, day: Int, month: Int, year: Int): Calendar {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.DAY_OF_MONTH, day)
        chore.expiration = calendar.time
        return calendar
    }
}