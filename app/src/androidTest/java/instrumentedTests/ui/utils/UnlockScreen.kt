package instrumentedTests.ui.utils

import android.app.Activity
import android.os.Build
import android.view.WindowManager

/**
 * Added Function to make Travis UI Tests to work hopefully
 * https://github.com/travis-ci/travis-ci/issues/6340#issuecomment-239537244
 * */
internal fun Activity.keepScreenActive() {
    val wakeUpDevice = Runnable {
        if (Build.VERSION.SDK_INT >= 26) {
            setShowWhenLocked(true)
            setTurnScreenOn(true)
            this.window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        } else {
            this.window.addFlags(
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON or
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON)
        }
    }
    this.runOnUiThread(wakeUpDevice)
}