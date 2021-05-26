package com.gabr.gabc.kelo.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * ViewModel class that serves to control the variable load between Settings and MainActivity
 * */
class LoadViewModel : ViewModel() {
    private val _isLoading: MutableLiveData<Boolean> = MutableLiveData()
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    /**
     * Sets the loading mode in the desired view model [MutableLiveData] variable
     *
     * @param loading: value to be set
     */
    fun setLoading(loading: Boolean) { _isLoading.postValue(loading) }
}