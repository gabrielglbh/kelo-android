package com.gabr.gabc.kelo.utils

import android.app.Activity
import android.app.AlertDialog
import com.gabr.gabc.kelo.R

/** Singleton instance with helper functions to create Dialogs across all code */
object DialogSingleton {
    /**
     * Creates and shows a customizable dialog with a various parameters:
     *
     * @param activity: current activity to show the dialog on
     * @param title: title of the dialog
     * @param message: message to show in the dialog body
     * @param positiveButton: text to display in the positive button
     * @param onPositive: function that executes when the positive button is pressed
     * */
    fun createCustomDialog(activity: Activity, title: String, message: String,
                           positiveButton: String, onPositive: () -> Unit) {
        val builder = AlertDialog.Builder(activity)
        builder.setTitle(title)
            .setMessage(message)
            .setPositiveButton(positiveButton) { dialog, _ ->
                onPositive()
                dialog.dismiss()
            }
            .setNegativeButton(
                activity.baseContext.getString(R.string.settings_dialog_btn_negative)
            ) { dialog, _ -> dialog.dismiss() }
        builder.create().show()
    }
}