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

    /** Adds all chores stated by the parameter to the current live data object */
    fun addAllChores(chores: ArrayList<Chore>) {
        _choreList.postValue(chores)
    }
}