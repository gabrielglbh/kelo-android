package com.gabr.gabc.kelo.utils.widgets

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import com.gabr.gabc.kelo.R
import java.util.*

/**
 * @param calendar: previous selected calendar to set this dialog to it
 * @param listener: listener to manage the selected date on the caller
 * */
class CustomDatePicker(val calendar: Calendar, val listener: (day: Int, month: Int, year: Int) -> Unit) :
    DialogFragment(), DatePickerDialog.OnDateSetListener {

    companion object {
        const val TAG = "DatePicker"
    }

    /**
     * Creates the DatePicker. Retrieves the current selected date from previous openings
     * and sets thee minimum and maximum dates based on today
     * */
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val picker = DatePickerDialog(requireContext(), R.style.CustomDatePicker, this, year, month, day)

        val c = Calendar.getInstance()
        picker.datePicker.minDate = c.timeInMillis
        c.add(Calendar.YEAR, 2)
        picker.datePicker.maxDate = c.timeInMillis
        return picker
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        listener(dayOfMonth, month, year)
    }
}