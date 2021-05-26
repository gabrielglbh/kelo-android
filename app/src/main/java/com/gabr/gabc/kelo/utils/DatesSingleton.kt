package com.gabr.gabc.kelo.utils

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
        lateinit var dayOfWeek: String
        lateinit var month: String
        val ln = Locale.getDefault().language

        when (calendar.get(Calendar.MONTH)) {
            Calendar.JANUARY -> month = if (ln == "en") "January" else "Enero"
            Calendar.FEBRUARY -> month = if (ln == "en") "February" else "Febrero"
            Calendar.MARCH -> month = if (ln == "en") "March" else "Marzo"
            Calendar.APRIL -> month = if (ln == "en") "April" else "Abril"
            Calendar.MAY -> month = if (ln == "en") "May" else "Mayo"
            Calendar.JUNE -> month = if (ln == "en") "June" else "Junio"
            Calendar.JULY -> month = if (ln == "en") "July" else "Julio"
            Calendar.AUGUST -> month = if (ln == "en") "August" else "Agosto"
            Calendar.SEPTEMBER -> month = if (ln == "en") "September" else "Septiembre"
            Calendar.OCTOBER -> month = if (ln == "en") "October" else "Octubre"
            Calendar.NOVEMBER -> month = if (ln == "en") "November" else "Noviembre"
            Calendar.DECEMBER -> month = if (ln == "en") "December" else "Diciembre"
        }
        when (calendar.get(Calendar.DAY_OF_WEEK)) {
            Calendar.MONDAY -> dayOfWeek = if (ln == "en") "Monday" else "Lunes"
            Calendar.TUESDAY -> dayOfWeek = if (ln == "en") "Tuesday" else "Martes"
            Calendar.WEDNESDAY -> dayOfWeek = if (ln == "en") "Wednesday" else "Miércoles"
            Calendar.THURSDAY -> dayOfWeek = if (ln == "en") "Thursday" else "Jueves"
            Calendar.FRIDAY -> dayOfWeek = if (ln == "en") "Friday" else "Viernes"
            Calendar.SATURDAY -> dayOfWeek = if (ln == "en") "Saturday" else "Sábado"
            Calendar.SUNDAY -> dayOfWeek = if (ln == "en") "Sunday" else "Domingo"
        }

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