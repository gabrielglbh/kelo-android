package com.gabr.gabc.kelo.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import com.gabr.gabc.kelo.R
import com.gabr.gabc.kelo.dataModels.User
import com.gabr.gabc.kelo.firebase.UserQueries
import com.gabr.gabc.kelo.utils.SharedPreferences
import com.gabr.gabc.kelo.utils.UtilsSingleton
import com.gabr.gabc.kelo.welcome.WelcomePageFunctions
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/** Bottom Sheet Dialog Fragment that holds the share code view in MainActivity */
class UserNameBottomSheet(private val user: User) : BottomSheetDialogFragment() {

    private lateinit var layout: TextInputLayout
    private lateinit var edit: TextInputEditText

    companion object {
        const val TAG = "change_user_name_bottom_sheet"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.change_user_name_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        layout = view.findViewById(R.id.settingsChangeUserNameLayout)
        edit = view.findViewById(R.id.settingsChangeUserNameEditText)

        edit.setText(user.name)
        edit.doOnTextChanged { _, _, _, _ -> UtilsSingleton.clearErrorFromTextLayout(layout) }
        edit.setOnEditorActionListener { v, _, _ ->
            val name = edit.text.toString()
            if (!WelcomePageFunctions.isUserNameValid(name)) layout.error = getString(R.string.err_invalid_name)
            else {
                CoroutineScope(Dispatchers.Main).launch {
                    UtilsSingleton.clearErrorFromTextLayout(layout)
                    val success = UserQueries().isUsernameAvailable(SharedPreferences.groupId, name)
                    if (success) {
                        user.name = name
                        val updated = UserQueries().updateUser(user, SharedPreferences.groupId)
                        if (updated) dismiss()
                        else layout.error = getString(R.string.err_user_update)
                    } else layout.error = getString(R.string.err_username_taken)
                }
            }
            UtilsSingleton.hideKeyboard(requireActivity(), v)
            true
        }
    }
}