package com.gabr.gabc.kelo.main.choreList

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.gabr.gabc.kelo.R
import com.gabr.gabc.kelo.choreDetail.ChoreDetailActivity
import com.gabr.gabc.kelo.firebase.ChoreQueries
import com.gabr.gabc.kelo.firebase.UserQueries
import com.gabr.gabc.kelo.dataModels.Chore
import com.gabr.gabc.kelo.utils.PermissionsSingleton
import com.gabr.gabc.kelo.utils.SharedPreferences
import com.gabr.gabc.kelo.utils.UtilsSingleton
import com.gabr.gabc.kelo.viewModels.ChoreListViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/** Fragment that manages the list of chores from a group */
class ChoreList : Fragment(), ChoreListAdapter.ChoreListener {

    private lateinit var choreList: RecyclerView
    private lateinit var addChore: FloatingActionButton
    private lateinit var refresh: SwipeRefreshLayout

    private lateinit var viewModel: ChoreListViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.chore_list, container, false)
    }

    override fun onResume() {
        super.onResume()
        getChores()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = run { ViewModelProvider(this).get(ChoreListViewModel::class.java) }
        setFAB(view)
        setRecyclerView(view)
        getChores()

        refresh = view.findViewById(R.id.choresRefresh)
        refresh.setOnRefreshListener { getChores() }
    }

    private fun getChores() {
        CoroutineScope(Dispatchers.Main).launch {
            val chores = ChoreQueries().getAllChores(SharedPreferences.groupId)
            if (chores != null) viewModel.addAllChores(chores)
            else UtilsSingleton.showSnackBar(requireView(), getString(R.string.err_loading_chores), anchorView = addChore)
            refresh.isRefreshing = false
        }
    }

    private fun setFAB(view: View) {
        addChore = view.findViewById(R.id.choreListFAB)
        addChore.setOnClickListener {
            val intent = Intent(requireContext(), ChoreDetailActivity::class.java)
            intent.putExtra(ChoreDetailActivity.VIEW_DETAILS, false)
            startActivity(intent)
        }
    }

    private fun setRecyclerView(view: View) {
        choreList = view.findViewById(R.id.choreListRecyclerView)
        choreList.layoutManager = LinearLayoutManager(requireContext())
        val adapter = ChoreListAdapter(this, viewModel, this, requireContext(),
            parent = requireView(), anchor = addChore)
        val swipeHelper = ItemTouchHelper(ChoreListSwipeController(0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT, adapter, requireContext()))
        choreList.adapter = adapter
        swipeHelper.attachToRecyclerView(choreList)
    }

    override fun onChoreClick(chore: Chore) {
        chore.creator?.let {
            CoroutineScope(Dispatchers.Main).launch {
                val user = UserQueries().getUser(SharedPreferences.userId, SharedPreferences.groupId)
                if (PermissionsSingleton.isUserChoreCreator(it) || PermissionsSingleton.isUserAdmin(user)) {
                    val intent = Intent(context, ChoreDetailActivity::class.java)
                    intent.putExtra(ChoreDetailActivity.VIEW_DETAILS, true)
                    intent.putExtra(ChoreDetailActivity.CHORE, chore)
                    ContextCompat.startActivity(requireContext(), intent, null)
                } else {
                    UtilsSingleton.showSnackBar(requireView(), getString(R.string.permission_modify_chore),
                        anchorView = addChore)
                }
            }
        }
    }
}