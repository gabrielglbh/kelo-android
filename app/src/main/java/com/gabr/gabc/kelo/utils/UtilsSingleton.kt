package com.gabr.gabc.kelo.utils

import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.gabr.gabc.kelo.R
import com.gabr.gabc.kelo.utils.textDrawable.ColorGenerator
import com.gabr.gabc.kelo.utils.textDrawable.TextDrawable
import com.google.android.material.textfield.TextInputLayout
import java.util.*

/** Singleton instance with helper functions useful to all code */
object UtilsSingleton {
    /**
     * Clears the error from an specified layout
     *
     * @param layout: layout to  be cleaned
     * */
    fun clearErrorFromTextLayout(layout: TextInputLayout) {
        if (!layout.error.isNullOrEmpty()) layout.error = null
    }

    /**
     * Helper method to create the [ObjectAnimator] for all the views in this Fragment
     *
     * @param view: view to animate
     * @param d: duration of the animation
     * @param begin: offset in which the animation should begin
     * */
    fun createObjectAnimator(view: View, d: Long, begin: Float) {
        ObjectAnimator.ofFloat(view, "translationX", begin, 0f).apply {
            duration = d
            start()
        }
    }

    /**
     * Hide the keyboard of the current view
     *
     * @param activity: Current activity
     * @param view: Current view
     * */
    fun hideKeyboard(activity: Activity?, view: View) {
        val inputMethodManager = activity?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager?
        inputMethodManager?.hideSoftInputFromWindow(view.windowToken, 0)
    }

    /**
     * Changes the color of the status bar
     *
     * @param activity: current activity
     * @param context: current context of the activity
     * @param color: color to be set as the color of the status bar
     * */
    fun changeStatusBarColor(activity: Activity?, context: Context, color: Int) {
        activity?.window?.statusBarColor = ContextCompat.getColor(context, color)
    }

    /**
     * Disables or enables user interaction with the UI based on a Boolean
     *
     * @param activity: current activity in which disable or enable the UI
     * @param view: Current view
     * @param disable: Boolean defining whether to disable or enable the UI
     * */
    fun setUserInteractionWhileLoading(activity: Activity?, view: View?, disable: Boolean) {
        view?.let { hideKeyboard(activity, view) }
        val flag = WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        if (disable) activity?.window?.setFlags(flag, flag)
        else activity?.window?.clearFlags(flag)
    }

    /**
     * Manages the show/hide of the loading_widget
     *
     * @param view: loading widget to hide or show
     * @param fullView: Nullable view representing the full_view_loading_widget in order to
     * @param show: Boolean to whether show or hider the [view]
     * show or hide that instead of solely the [view]
     * */
    fun manageLoadingView(view: ProgressBar, fullView: ConstraintLayout?, show: Boolean) {
        if (show) view.visibility = View.VISIBLE
        else view.visibility = View.INVISIBLE

        if (fullView != null) {
            if (show) fullView.visibility = View.VISIBLE
            else fullView.visibility = View.INVISIBLE
        }
    }

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

    /**
     * Function that creates based on an ImageView the avatar for a element list
     *
     * @param name: name of the user or element label to gather the initial from
     * @return drawable to inject it in an ImageView
     * */
    fun createAvatar(name: String?): Drawable {
        val generator = ColorGenerator(ColorGenerator.MATERIAL)
        val color = generator.getColor(name)
        return TextDrawable.builder().buildRound(name?.first().toString(), color)!!
    }

    /**
     * Function that verifies if the current user is currently being displayed in screen
     *
     * @param userId: current user id
     * @return true if the userId matches the saved one in shared preferences
     * */
    fun isUserBeingDisplayedCurrentUser(userId: String): Boolean = userId == SharedPreferences.userId

    /**
     * Function that given a text view and an image view, sets those views to a personal refer.
     * textview -> 'You' and the icon (if any) -> R.drawable.person
     *
     * @param context: application context
     * @param textView: text view to override
     * @param imageView: image view to override
     * */
    fun setTextAndIconToYou(context: Context, textView: TextView, imageView: ImageView?) {
        textView.text = context.getString(R.string.users_you)
        imageView?.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.person))
    }
}