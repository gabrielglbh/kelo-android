package com.gabr.gabc.kelo.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gabr.gabc.kelo.constants.Constants
import com.gabr.gabc.kelo.utils.common.CurrencyModel

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
    val userName: LiveData<String>
        get() = _userName

    private val _groupName: MutableLiveData<String> = MutableLiveData()
    val groupName: LiveData<String>
        get() = _groupName

    private val _groupCode: MutableLiveData<String> = MutableLiveData()
    val groupCode: LiveData<String>
        get() = _groupCode

    private val _groupCurrency: MutableLiveData<CurrencyModel> = MutableLiveData()
    val groupCurrency: LiveData<CurrencyModel>
        get() = _groupCurrency

    private val _viewPagerPage: MutableLiveData<Int> = MutableLiveData()
    val viewPagerPage: LiveData<Int>
        get() = _viewPagerPage

    private val _groupSelectedMode: MutableLiveData<String> = MutableLiveData()
    val groupSelectedMode: LiveData<String>
        get() = _groupSelectedMode

    private val _isLoading: MutableLiveData<Boolean> = MutableLiveData()
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    init { _groupCurrency.postValue(Constants.CURRENCIES[0]) }

    /**
     * Sets the user name in the desired view model [MutableLiveData] variable
     *
     * @param name: value to be set
     */
    fun setUserName(name: String) { _userName.value = name }

    /**
     * Sets the group name in the desired view model [MutableLiveData] variable
     *
     * @param name: value to be set
     */
    fun setGroupName(name: String) { _groupName.value = name }

    /**
     * Sets the group code in the desired view model [MutableLiveData] variable
     *
     * @param code: value to be set
     */
    fun setGroupCode(code: String) { _groupCode.value = code }

    /**
     * Sets the group currency in the desired view model [MutableLiveData] variable
     *
     * @param currency: value to be set
     */
    fun setCurrency(currency: CurrencyModel) { _groupCurrency.postValue(currency) }

    /**
     * Sets the view pager page in the desired view model [MutableLiveData] variable
     *
     * @param page: value to be set
     */
    fun setPagerPage(page: Int) { _viewPagerPage.value = page }

    /**
     * Sets the mode the user selected (create or join) in the desired view model [MutableLiveData] variable
     *
     * @param mode: value to be set
     */
    fun setGroupMode(mode: String) { _groupSelectedMode.value = mode }

    /**
     * Sets the loading state in the desired view model [MutableLiveData] variable
     *
     * @param isLoading: value to be set
     */
    fun setLoading(isLoading: Boolean) { _isLoading.value = isLoading }
}