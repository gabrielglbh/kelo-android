package com.gabr.gabc.kelo.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gabr.gabc.kelo.utils.common.CurrencyModel

/**
 * ViewModel class that serves to control the variable load between Settings and MainActivity
 * */
class MainViewModel : ViewModel() {
    private val _isLoading: MutableLiveData<Boolean> = MutableLiveData()
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val _groupCurrency: MutableLiveData<CurrencyModel> = MutableLiveData()
    val groupCurrency: LiveData<CurrencyModel>
        get() = _groupCurrency

    /**
     * Sets the loading mode in the desired view model [MutableLiveData] variable
     *
     * @param loading: value to be set
     */
    fun setLoading(loading: Boolean) { _isLoading.postValue(loading) }

    /**
     * Sets the group currency in the desired view model [MutableLiveData] variable
     *
     * @param currency: value to be set
     */
    fun setCurrency(currency: CurrencyModel) { _groupCurrency.postValue(currency) }
}