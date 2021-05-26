package com.gabr.gabc.kelo.utils

import android.app.Activity
import android.view.View
import android.view.WindowManager
import android.widget.ProgressBar
import androidx.constraintlayout.widget.ConstraintLayout
import com.gabr.gabc.kelo.R

/** Singleton instance with helper functions for showing loading widgets or view accross all code */
object LoadingSingleton {
    /**
     * Function that handles the creation of a full screen loading view. It REQUIRES the inclusion
     * of the loading_widget layout in the XML in which the loading screen should be prompted.
     *
     * @param activity: current activity in which disable or enable the UI
     * @param parent: current parent view of the activity. The top View of the XML
     * @param progressBar: view containing the progress bar. ID: loadingWidget
     * @param fullView: view from the loading_widget. ID: loadingFragment
     * @param loading: Boolean defining whether to disable or enable the UI
     * */
    fun showFullLoadingScreen(activity: Activity?, parent: View?, progressBar: ProgressBar,
                              fullView: ConstraintLayout?, loading: Boolean) {
        setUserInteractionWhileLoading(activity, parent, loading)
        manageLoadingView(progressBar, fullView, loading)
        if (activity != null) {
            if (loading) UtilsSingleton.changeStatusBarColor(activity, activity.baseContext, R.color.secondaryColor)
            else UtilsSingleton.changeStatusBarColor(activity, activity.baseContext, R.color.contrast)
        }
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

    private fun setUserInteractionWhileLoading(activity: Activity?, view: View?, disable: Boolean) {
        view?.let { UtilsSingleton.hideKeyboard(activity, view) }
        val flag = WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        if (disable) activity?.window?.setFlags(flag, flag)
        else activity?.window?.clearFlags(flag)
    }
}