package com.gabr.gabc.kelo.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gabr.gabc.kelo.dataModels.Chore

/**
 * ViewModel that manages the local state of the chore list for the user. This enables the
 * proper functioning of the adapter's notifiers whenever a chore is updated, added or removed.
 * */
class ChoreListViewModel: ViewModel() {
    private val _choreList: MutableLiveData<ArrayList<Chore>> = MutableLiveData()
    val choreList: LiveData<ArrayList<Chore>>
        get() = _choreList

    private val _showCompleted: MutableLiveData<Boolean> = MutableLiveData()
    val showCompleted: LiveData<Boolean>
        get() = _showCompleted

    private val _showAssigned: MutableLiveData<Boolean> = MutableLiveData()
    val showAssigned: LiveData<Boolean>
        get() = _showAssigned

    private val _actionBarTitle: MutableLiveData<String> = MutableLiveData()
    val actionBarTitle: LiveData<String>
        get() = _actionBarTitle

    init {
        _showCompleted.postValue(false)
        _showAssigned.postValue(false)
    }

    /** Adds all chores stated by the parameter to the current live data object */
    fun addAllChores(chores: ArrayList<Chore>) {
        _choreList.postValue(chores)
    }

    /** Sets the mode for showing the chores depending of the value of the parameter [mode] */
    fun setShowCompleted(mode: Boolean) {
        _showCompleted.postValue(mode)
    }

    /** Sets the mode for showing the chores depending of the value of the parameter [mode] */
    fun setShowAssigned(mode: Boolean) {
        _showAssigned.postValue(mode)
    }

    /** Sets the title for the action bar when the chore list is changed */
    fun setActionBarTitle(title: String) {
        _actionBarTitle.postValue(title)
    }
}