package com.gabr.gabc.kelo.mainActivity.choreList

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gabr.gabc.kelo.ChoreDetailActivity
import com.gabr.gabc.kelo.R
import com.gabr.gabc.kelo.models.Chore
import com.gabr.gabc.kelo.utils.SharedPreferences
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ChoreList : Fragment(), ChoreListAdapter.ItemClickListener {

    private lateinit var choreList: RecyclerView
    private lateinit var addChore: FloatingActionButton
    private lateinit var loading: ProgressBar

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.chore_list, container, false)
    }

    /**
     * Sets up the Recycler View for the Chore lists and the FAB for adding chores
     *
     * @param view: current view
     * @param savedInstanceState: current bundle, if any
     * */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loading = view.findViewById(R.id.loadingWidget)

        setFAB(view)
        setRecyclerView(view)
    }

    /**
     * Initializes the RecyclerView.
     *
     * @param view: current view
     * */
    private fun setRecyclerView(view: View) {
        val adapter = ChoreListAdapter(this, requireContext(), loading, SharedPreferences.groupId)
        val swipeHelper = ItemTouchHelper(ChoreListSwipeController(0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT, adapter, requireContext()))

        choreList = view.findViewById(R.id.choreListRecyclerView)
        choreList.layoutManager = LinearLayoutManager(requireContext())
        choreList.adapter = adapter
        swipeHelper.attachToRecyclerView(choreList)
    }

    /**
     * Initializes and sets the click listener to the Add Chore FAB
     *
     * @param view: current view
     * */
    private fun setFAB(view: View) {
        addChore = view.findViewById(R.id.choreListFAB)
        addChore.setOnClickListener {
            val intent = Intent(requireContext(), ChoreDetailActivity::class.java)
            intent.putExtra(ChoreDetailActivity.VIEW_DETAILS, false)
            startActivity(intent)
        }
    }

    /**
     * Overrides from [ChoreListAdapter.ItemClickListener]. Triggered whenever an item of the
     * RecyclerView [choreList] is clicker, gathering the chore itself. It navigates to
     * [ChoreDetailActivity] with the selected chore.
     *
     * @param view: current view
     * @param chore: clicked chore
     * */
    override fun itemClickInPosition(view: View?, chore: Chore) {
        val intent = Intent(context, ChoreDetailActivity::class.java)
        intent.putExtra(ChoreDetailActivity.VIEW_DETAILS, true)
        intent.putExtra(ChoreDetailActivity.CHORE, chore)
        ContextCompat.startActivity(requireContext(), intent, null)
    }
}