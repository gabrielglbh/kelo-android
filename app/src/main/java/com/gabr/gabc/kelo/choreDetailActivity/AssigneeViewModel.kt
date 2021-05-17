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
    var assignee: LiveData<User> = _assignee

    fun setAssignee(user: User) { _assignee.postValue(user) }
    fun getAssignee(): User = assignee.value!!
}