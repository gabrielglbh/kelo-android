package com.gabr.gabc.kelo.utils

import com.gabr.gabc.kelo.constants.Constants
import java.util.*

/** Singleton instance with helper functions to parse dates across all code */
object DatesSingleton {
    /**
     * Parses a [Calendar] object to a recognizable [String]
     *
     * @param calendar: customized calendar to be parsed
     * @return A recognizable String formatted as DAY/MONTH/YEAR
     * */
    fun parseCalendarToString(calendar: Calendar): String {
        var ln = Locale.getDefault().language
        if (ln != "es" && ln != "en") ln = "en"

        val month = Constants.MONTHS[ln]?.get(calendar.get(Calendar.MONTH))
        val dayOfWeek = Constants.DAY_OF_WEEK[ln]?.get(calendar.get(Calendar.DAY_OF_WEEK))

        return "$dayOfWeek, ${calendar.get(Calendar.DAY_OF_MONTH)} $month ${calendar.get(Calendar.YEAR)}"
    }

    /**
     * Parses a [Calendar] object to a recognizable [String] for the display on the Chore List
     *
     * @param calendar: customized calendar to be parsed
     * @return A recognizable String formatted as DAY/MONTH/YEAR
     * */
    fun parseCalendarToStringOnList(calendar: Calendar): String {
        val year = calendar.get(Calendar.YEAR).toString().substring(2, 4)
        return "${calendar.get(Calendar.DAY_OF_MONTH)}/${calendar.get(Calendar.MONTH)+1}/$year"
    }
}