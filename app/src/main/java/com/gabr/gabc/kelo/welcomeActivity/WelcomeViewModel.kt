package com.gabr.gabc.kelo.welcomeActivity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gabr.gabc.kelo.constants.CURRENCIES
import com.gabr.gabc.kelo.welcomeActivity.viewBottomSheet.CurrencyModel

/**
 * ViewModel that handles the login form
 * From changing pages in the ViewPager2 to validating
 *
 * LiveData is for getting the non-mutable object and to not expose the Mutable Object for
 * evading possible accidental modifications
 *
 * MutableLiveData is used for changing the actual value of the object
 * */
class WelcomeViewModel: ViewModel() {
    private val _userName: MutableLiveData<String> = MutableLiveData()
    var userName: LiveData<String> = _userName

    private val _groupName: MutableLiveData<String> = MutableLiveData()
    var groupName: LiveData<String> = _groupName
    private val _groupCode: MutableLiveData<String> = MutableLiveData()
    var groupCode: LiveData<String> = _groupCode
    private val _groupCurrency: MutableLiveData<CurrencyModel> = MutableLiveData()
    var groupCurrency: LiveData<CurrencyModel> = _groupCurrency

    /**
     * It serves as a controller for showing or changing the ViewPager2 State
     */
    private val _viewPagerMode: MutableLiveData<Int> = MutableLiveData()
    var viewPagerMode: LiveData<Int> = _viewPagerMode

    /**
     * Ir serves as a controller for letting us know if the user is joining or creating a group
     * */
    private val _groupMode: MutableLiveData<String> = MutableLiveData()
    var groupMode: LiveData<String> = _groupMode

    /**
     * Variable for showing the loading view
     * */
    private val _isLoading: MutableLiveData<Boolean> = MutableLiveData()
    var isLoading: LiveData<Boolean> = _isLoading

    init { _groupCurrency.postValue(CURRENCIES[0]) }

    fun setUserName(name: String) { _userName.postValue(name) }
    fun setGroupName(name: String) { _groupName.postValue(name) }
    fun setGroupCode(code: String) { _groupCode.postValue(code) }
    fun setCurrency(currency: CurrencyModel) { _groupCurrency.postValue(currency) }
    fun setPagerPage(page: Int) { _viewPagerMode.postValue(page) }
    fun setGroupMode(mode: String) { _groupMode.postValue(mode) }
    fun setLoading(isLoading: Boolean) { _isLoading.postValue(isLoading) }

    fun getUserName(): String = userName.value!!
    fun getGroupName(): String = groupName.value!!
    fun getGroupCode(): String = groupCode.value!!
    fun getCurrency(): CurrencyModel = groupCurrency.value!!
    fun getCurrentPage(): Int = viewPagerMode.value!!
}