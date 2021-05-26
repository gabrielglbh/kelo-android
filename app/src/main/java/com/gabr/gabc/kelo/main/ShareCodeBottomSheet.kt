package com.gabr.gabc.kelo.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.gabr.gabc.kelo.R
import com.gabr.gabc.kelo.utils.SharedPreferences
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton

/** Bottom Sheet Dialog Fragment that holds the share code view in MainActivity */
class ShareCodeBottomSheet : BottomSheetDialogFragment() {

    private lateinit var groupCode: TextView
    private lateinit var shareCodeButton: MaterialButton

    companion object {
        const val TAG = "share_code_bottom_sheet"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.share_code_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        groupCode = view.findViewById(R.id.bsGroupCode)
        shareCodeButton = view.findViewById(R.id.bsShareGroupCodeButton)

        groupCode.text = SharedPreferences.groupId

        shareCodeButton.setOnClickListener {
            val intent = Intent().setAction(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, "${getString(R.string.message_on_code_sharing)}\n" +
                    "${getString(R.string.deepLinkJoinGroup)}${SharedPreferences.groupId}")
            startActivity(Intent.createChooser(intent, getString(R.string.share_group_code)))
        }
    }
}