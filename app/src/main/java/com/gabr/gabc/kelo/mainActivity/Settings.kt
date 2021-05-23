package com.gabr.gabc.kelo.mainActivity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.gabr.gabc.kelo.R
import com.gabr.gabc.kelo.utils.SharedPreferences
import com.google.android.material.button.MaterialButton

/** Fragment that manages all settings of Kelo */
class Settings : Fragment() {

    private lateinit var debugButton: MaterialButton

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        debugButton = view.findViewById(R.id.settingsDebugButton)
        debugButton.setOnClickListener {
            SharedPreferences.putIsFirstLaunched(requireActivity(), false)
        }
    }
}