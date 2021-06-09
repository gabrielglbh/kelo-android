package com.gabr.gabc.kelo.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.*

/**
 * ViewModel class that serves to control the variable periodicity between PeriodicityBottomSheet
 * and RewardActivity
 * */
class RewardViewModel : ViewModel() {
    private val _periodicity: MutableLiveData<Int> = MutableLiveData()
    val periodicity: LiveData<Int>
        get() = _periodicity

    /**
     * Sets the user in the desired view model [MutableLiveData] variable
     *
     * @param periodicity: value to be set
     */
    fun setPeriodicity(periodicity: Int) { _periodicity.postValue(periodicity) }
}