package com.gabr.gabc.kelo.choreDetailActivity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gabr.gabc.kelo.models.User

/**
 * ViewModel class that serves to control the variable assignee between UsersBottomSheet
 * and ChoreDetailActivity
 * */
class AssigneeViewModel : ViewModel() {
    private val _assignee: MutableLiveData<User> = MutableLiveData()
    val assignee: LiveData<User>
        get() = _assignee

    /**
     * Sets the user in the desired view model [MutableLiveData] variable
     *
     * @param user: value to be set
     */
    fun setAssignee(user: User) { _assignee.postValue(user) }
}