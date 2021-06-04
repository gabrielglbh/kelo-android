package com.gabr.gabc.kelo.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gabr.gabc.kelo.dataModels.User

/**
 * ViewModel that manages the local state of the user list for the user. This enables the
 * proper functioning of the adapter's notifiers whenever a chore is updated, added or removed.
 * */
class UserListViewModel: ViewModel() {
    private val _userList: MutableLiveData<ArrayList<User>> = MutableLiveData()
    val userList: LiveData<ArrayList<User>>
        get() = _userList

    /** Adds all users stated by the parameter to the current live data object */
    fun addAllUsers(users: ArrayList<User>) {
        _userList.postValue(users)
    }
}