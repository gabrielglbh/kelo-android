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

/** Class defining the first page of the tutorial view pager */
class TutorialPager1 : Fragment()  {

    private lateinit var tutorialTextView: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.tutorial_pager_page_1, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tutorialTextView = view.findViewById(R.id.tutorial1)
        val sp = SpannableString(getString(R.string.tutorial_1_msg_1))
        val shareIcon = ContextCompat.getDrawable(requireContext(), R.drawable.share)
        shareIcon?.setBounds(0, 0, shareIcon.intrinsicWidth, shareIcon.intrinsicHeight)
        shareIcon?.let { icon ->
            if (resources.configuration.locales.get(0) == Locale("es", "ES")) {
                sp.setSpan(ImageSpan(icon, ImageSpan.ALIGN_BASELINE), 58, 59, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            } else {
                sp.setSpan(ImageSpan(icon, ImageSpan.ALIGN_BASELINE), 54, 55, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
        }
        tutorialTextView.text = sp
    }
}