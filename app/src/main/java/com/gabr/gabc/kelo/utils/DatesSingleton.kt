package com.gabr.gabc.kelo.utils

import android.content.Context
import com.gabr.gabc.kelo.R
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

    /**
     * Function that depending on the selected periodicity of the reward, returns a [Date] representing
     * said periodicity starting 'today'
     *
     * @param mode: selected periodicity
     * @return [Date] object representing the periodicity
     * */
    fun getDateFromMode(mode: Int): Date? {
        val date = Calendar.getInstance()
        when (mode) {
            Constants.NO_FREQUENCY -> return null
            Constants.WEEKLY -> date.add(Calendar.WEEK_OF_YEAR, 1)
            Constants.EVERY_TWO_WEEKS -> date.add(Calendar.WEEK_OF_YEAR, 2)
            Constants.MONTHLY -> date.add(Calendar.MONTH, 1)
            Constants.EVERY_TWO_MONTHS -> date.add(Calendar.MONTH, 2)
            Constants.ANNUALLY -> date.add(Calendar.YEAR, 1)
        }
        return date.time
    }

    /**
     * Depending of the mode of the selected periodicity, returns an specific string for updating
     * the UI accordingly
     *
     * @param context: current context
     * @param mode: selected periodicity
     * @return string defined for each available periodicity
     * */
    fun getStringFromMode(context: Context, mode: Int): String {
        return when (mode) {
            Constants.NO_FREQUENCY -> context.getString(R.string.rewards_no_frequency)
            Constants.WEEKLY -> context.getString(R.string.rewards_weekly)
            Constants.EVERY_TWO_WEEKS -> context.getString(R.string.rewards_two_weeks)
            Constants.MONTHLY -> context.getString(R.string.rewards_monthly)
            Constants.EVERY_TWO_MONTHS -> context.getString(R.string.rewards_two_months)
            Constants.ANNUALLY -> context.getString(R.string.rewards_annually)
            else -> context.getString(R.string.chore_expire_date_placeholder)
        }
    }
}