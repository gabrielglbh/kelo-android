package com.gabr.gabc.kelo.utils

import java.util.*

/** Singleton instance with helper functions to parse dates across all code */
object DatesSingleton {
    private val months : Map<String, Map<Int, String>> = hashMapOf(
        "en" to hashMapOf(
            0 to "January", 1 to "February", 2 to "March",
            3 to "April", 4 to "May", 5 to "June",
            6 to "July", 7 to "August", 8 to "September",
            9 to "October", 10 to "November", 11 to "December"
        ),
        "es" to hashMapOf(
            0 to "Enero", 1 to "Febrero", 2 to "Marzo",
            3 to "Abril", 4 to "Mayo", 5 to "Junio",
            6 to "Julio", 7 to "Agosto", 8 to "Septiembre",
            9 to "Octubre", 10 to "Noviembre", 11 to "Diciembre"
        )
    )

    private val dayOfWeek : Map<String, Map<Int, String>> = hashMapOf(
        "en" to hashMapOf(
            1 to "Sunday", 2 to "Monday",
            3 to "Tuesday", 4 to "Wednesday", 5 to "Thursday",
            6 to "Friday", 7 to "Saturday"
        ),
        "es" to hashMapOf(
            1 to "Domingo", 2 to "Lunes",
            3 to "Martes", 4 to "Miércoles", 5 to "Jueves",
            6 to "Viernes", 7 to "Sábado"
        )
    )

    /**
     * Parses a [Calendar] object to a recognizable [String]
     *
     * @param calendar: customized calendar to be parsed
     * @return A recognizable String formatted as DAY/MONTH/YEAR
     * */
    fun parseCalendarToString(calendar: Calendar): String {
        var ln = Locale.getDefault().language
        if (ln != "es" && ln != "en") ln = "en"

        val month = months[ln]?.get(calendar.get(Calendar.MONTH))
        val dayOfWeek = dayOfWeek[ln]?.get(calendar.get(Calendar.DAY_OF_WEEK))

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