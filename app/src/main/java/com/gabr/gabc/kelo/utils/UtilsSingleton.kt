package com.gabr.gabc.kelo.utils

import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Context
import android.graphics.Canvas
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import com.gabr.gabc.kelo.R
import com.gabr.gabc.kelo.utils.textDrawable.ColorGenerator
import com.gabr.gabc.kelo.utils.textDrawable.TextDrawable
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout

/** Singleton instance with helper functions useful to all code */
object UtilsSingleton {
    /**
     * Shows a snack bar with a desired message for additional information to be shown to the user
     *
     * @param view: view in which to show the snack bar
     * @param msg: message to show in the snack bar, the info
     * @param duration: duration the snack bar appears on the screen. By default LENGTH_SHORT
     * @param anchorView: view to set the snack bar above it
     * */
    fun showSnackBar(view: View, msg: String, duration: Int? = Snackbar.LENGTH_SHORT, anchorView: View? = null) {
        val snack = Snackbar.make(view, msg, duration!!)
        snack.anchorView = anchorView
        snack.show()
    }

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
     * Function that given a text view and an image view, sets those views to a personal refer.
     * textview -> 'You'
     *
     * @param context: application context
     * @param name: user name
     * @return parsed name: name (you)
     * */
    fun setTextForCurrentUser(context: Context, name: String): String {
        val you = context.getString(R.string.users_you)
        return "$name ($you)"
    }

    /**
     * Paints the background of the dragged item in a RecyclerView with a custom item and backgroun color
     *
     * @param canvas: actual container to draw the stuff
     * @param context: current context
     * @param dX: axis in which the swipe is being performed
     * @param itemView: holder of the RecyclerView
     * @param dualIcon: boolean to tell the swipe controller to paint two different icons when
     * swiping LEFT or RIGHT
     * */
    fun setUpSwipeController(canvas: Canvas, context: Context, dX: Float, itemView: View, dualIcon: Boolean) {
        lateinit var icon: Drawable
        lateinit var background: ColorDrawable

        if (dualIcon) {
            background = ColorDrawable(context.getColor(if (dX > 0) R.color.primaryColor else R.color.errorColor))
            ContextCompat.getDrawable(context, if (dX > 0) R.drawable.ic_completed else R.drawable.ic_delete)?.let { icon = it }
        } else {
            background = ColorDrawable(context.getColor(R.color.errorColor))
            ContextCompat.getDrawable(context, R.drawable.ic_delete)?.let { icon = it }
        }

        val iconHeight = icon.intrinsicHeight
        val iconWidth = icon.intrinsicWidth

        val top = itemView.top + 5
        val bottom = itemView.bottom - 5

        val iconMargin = (itemView.height - iconHeight) / 2
        val iconTop = itemView.top + iconMargin
        val iconBottom = iconTop + iconHeight

        when {
            dX > 0 -> {
                icon.colorFilter = PorterDuffColorFilter(
                    ContextCompat.getColor(context, R.color.swapIconColor),
                    PorterDuff.Mode.SRC_IN)
                val iconLeft = itemView.left + iconMargin
                val iconRight = iconLeft + iconWidth
                icon.setBounds(iconLeft, iconTop, iconRight, iconBottom)
                background.setBounds(itemView.left, top, itemView.left + dX.toInt(), bottom)
            }
            dX < 0 -> {
                val iconLeft = itemView.right - iconMargin - iconWidth
                val iconRight = itemView.right - iconMargin
                icon.setBounds(iconLeft, iconTop, iconRight, iconBottom)
                background.setBounds(itemView.right + dX.toInt(), top, itemView.right, bottom)
            }
            else -> background.setBounds(0, 0, 0, 0)
        }

        background.draw(canvas)
        icon.draw(canvas)
    }
}