package instrumentedTests.ui.utils

import android.content.res.Resources
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.test.espresso.matcher.BoundedMatcher
import org.hamcrest.Description
import org.hamcrest.Matcher

/**
 * Class that extends [Matcher] in order to check if an item has a given color
 * https://stackoverflow.com/questions/28742495/testing-background-color-espresso-android
 * */
class ColorViewMatcher {
    companion object {
        /**
         * Functions that validates if an specified layout or view has a background color or not
         *
         * @param expectedResourceId: layout or view to be asserted
         * */
        fun matchesBackgroundColor(expectedResourceId: Int): Matcher<View?> {
            return object : BoundedMatcher<View?, View?>(View::class.java) {
                var actualColor = 0
                var expectedColor = 0
                var message: String? = null

                override fun matchesSafely(item: View?): Boolean {
                    if (item?.background == null) {
                        message = item?.id.toString() + " does not have a background"
                        return false
                    }
                    val resources: Resources = item.context.resources
                    expectedColor = ResourcesCompat.getColor(resources, expectedResourceId, null)
                    actualColor = try {
                        (item.background as ColorDrawable).color
                    } catch (e: Exception) {
                        (item.background as GradientDrawable).color!!.defaultColor
                    }
                    return actualColor == expectedColor
                }

                override fun describeTo(description: Description) {
                    if (actualColor != 0) {
                        message = ("Background color did not match: Expected "
                                + String.format(
                            "#%06X",
                            (0xFFFFFF and expectedColor)
                        ) + " was " + String.format("#%06X", (0xFFFFFF and actualColor)))
                    }
                    description.appendText(message)
                }
            }
        }
    }
}