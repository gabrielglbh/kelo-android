package com.gabr.gabc.kelo.main.choreList

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.gabr.gabc.kelo.R
import com.gabr.gabc.kelo.choreDetail.ChoreDetailActivity
import com.gabr.gabc.kelo.firebase.ChoreQueries
import com.gabr.gabc.kelo.models.Chore
import com.gabr.gabc.kelo.utils.PermissionsSingleton
import com.gabr.gabc.kelo.utils.SharedPreferences
import com.gabr.gabc.kelo.utils.UtilsSingleton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/** Fragment that manages the list of chores from a group */
class ChoreList : Fragment(), ChoreListAdapter.ChoreListener {

    private lateinit var choreList: RecyclerView
    private lateinit var addChore: FloatingActionButton
    private lateinit var refresh: SwipeRefreshLayout

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.chore_list, container, false)
    }

    override fun onResume() {
        super.onResume()
        getChores()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setFAB(view)

        choreList = view.findViewById(R.id.choreListRecyclerView)
        choreList.layoutManager = LinearLayoutManager(requireContext())
        getChores()

        refresh = view.findViewById(R.id.choresRefresh)
        refresh.setOnRefreshListener { getChores() }
    }

    private fun getChores() {
        CoroutineScope(Dispatchers.Main).launch {
            val chores = ChoreQueries().getAllChores(SharedPreferences.groupId)
            if (chores != null) setRecyclerView(chores)
            else UtilsSingleton.showSnackBar(requireView(), getString(R.string.err_loading_chores), anchorView = addChore)
            refresh.isRefreshing = false
        }
    }

    private fun setRecyclerView(chores: ArrayList<Chore>) {
        try {
            val adapter = ChoreListAdapter(
                chores, this, requireContext(),
                parent = requireView(), anchor = addChore)
            val swipeHelper = ItemTouchHelper(
                ChoreListSwipeController(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT, adapter, requireContext()))

            choreList.adapter = adapter
            swipeHelper.attachToRecyclerView(choreList)
        } catch (e: IllegalStateException) {
            Log.e("CONTEXT ERROR", e.message.toString())
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

    override fun onChoreClick(chore: Chore) {
        chore.creator?.let {
            if (PermissionsSingleton.isUserChoreCreator(it)) {
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

    override fun updateChores() { getChores() }
}