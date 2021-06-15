package com.gabr.gabc.kelo.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gabr.gabc.kelo.dataModels.Reward

/**
 * ViewModel class that serves to control the variable periodicity between PeriodicityBottomSheet
 * and RewardActivity
 * */
class RewardViewModel : ViewModel() {
    private val _periodicity: MutableLiveData<Reward.Frequencies> = MutableLiveData()
    val periodicity: LiveData<Reward.Frequencies>
        get() = _periodicity

    /**
     * Sets the user in the desired view model [MutableLiveData] variable
     *
     * @param periodicity: value to be set
     */
    fun setPeriodicity(periodicity: Reward.Frequencies) { _periodicity.postValue(periodicity) }
}