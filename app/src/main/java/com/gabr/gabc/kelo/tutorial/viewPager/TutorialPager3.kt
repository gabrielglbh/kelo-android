package com.gabr.gabc.kelo.tutorial.viewPager

import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ImageSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.gabr.gabc.kelo.R
import java.util.*

/** Class defining the third page of the tutorial view pager */
class TutorialPager3 : Fragment()  {

    private lateinit var tutorialTextView: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.tutorial_pager_page_3, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tutorialTextView = view.findViewById(R.id.tutorial2)
        val sp = SpannableString(getString(R.string.tutorial_3_msg_2))
        val completedIcon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_completed)
        completedIcon?.setBounds(0, 0, completedIcon.intrinsicWidth, completedIcon.intrinsicHeight)
        completedIcon?.let { icon ->
            if (resources.configuration.locales.get(0) == Locale("es", "ES")) {
                sp.setSpan(ImageSpan(icon, ImageSpan.ALIGN_BASELINE), 150, 151, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            } else {
                sp.setSpan(ImageSpan(icon, ImageSpan.ALIGN_BASELINE), 140, 141, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
        }
        tutorialTextView.text = sp
    }
}