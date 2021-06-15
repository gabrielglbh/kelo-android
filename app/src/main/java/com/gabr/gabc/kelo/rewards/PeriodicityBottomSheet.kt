package com.gabr.gabc.kelo.rewards

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gabr.gabc.kelo.R
import com.gabr.gabc.kelo.dataModels.Reward
import com.gabr.gabc.kelo.viewModels.RewardViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

/** Bottom Sheet Dialog Fragment that holds the list for the periodicity options buttons in RewardsActivity */
class PeriodicityBottomSheet : BottomSheetDialogFragment(), PeriodicityAdapter.RewardPeriodicityListener {
    companion object {
        const val TAG = "periodicity_bottom_sheet"
    }

    private lateinit var viewModel: RewardViewModel

    private lateinit var periodicityRecyclerView: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.periodicity_bottom_sheet, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = activity?.run { ViewModelProvider(this).get(RewardViewModel::class.java) }!!
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        periodicityRecyclerView = view.findViewById(R.id.periodicityRecyclerView)
        periodicityRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        periodicityRecyclerView.adapter = PeriodicityAdapter(requireContext(), this)
    }

    override fun onPeriodicityClick(mode: Int) {
        viewModel.setPeriodicity(Reward.Frequencies.values()[mode])
        dismiss()
    }
}