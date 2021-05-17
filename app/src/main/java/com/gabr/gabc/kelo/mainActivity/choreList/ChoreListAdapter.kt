package com.gabr.gabc.kelo.mainActivity.choreList

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.gabr.gabc.kelo.R
import com.gabr.gabc.kelo.firebase.ChoreQueries
import com.gabr.gabc.kelo.firebase.UserQueries
import com.gabr.gabc.kelo.models.Chore
import com.gabr.gabc.kelo.utils.SharedPreferences
import com.gabr.gabc.kelo.utils.UtilsSingleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

/**
 * Adapter for the Chore List.
 *
 * @param context: activity's context
 * @param loading: [ProgressBar] widget for showing it and hide it when loading
 * @param groupId: group id to retrieve the chores from
 * */
class ChoreListAdapter(private val listener: ItemClickListener, private val context: Context,
                       loading: ProgressBar, groupId: String?)
    : RecyclerView.Adapter<ChoreListAdapter.ChoreHolder>() {

    interface ItemClickListener { fun itemClickInPosition(view: View?, chore: Chore) }
    var chores: ArrayList<Chore> = arrayListOf()

    /**
     * Initializes the listener to the chores list in Firebase. It updates
     * the list every time the collection changes
     * */
    init {
        if (groupId != null) {
            UtilsSingleton.manageLoadingView(loading, null, true)
            val listener = ChoreQueries().attachListenerToChores(groupId,
                { position, chore -> addChore(position, chore) },
                { position, chore -> updateChore(position, chore) },
                { position -> removeChore(position) })

            if (listener == null) Toast.makeText(context, R.string.err_loading_chores, Toast.LENGTH_SHORT).show()
            UtilsSingleton.manageLoadingView(loading, null, false)
        } else Toast.makeText(context, R.string.err_group_does_not_exist, Toast.LENGTH_SHORT).show()
    }

    /**
     * Adds a [Chore] to the list and notifies the Recycler View to update its content
     *
     * @param position: position in which to include the new item
     * @param chore: chore to add
     * */
    private fun addChore(position: Int, chore: Chore) {
        chores.add(position, chore)
        notifyItemInserted(position)
    }

    /**
     * Modifies a [Chore] to the list and notifies the Recycler View to update its content
     *
     * @param position: position in which to update the item
     * @param chore: chore to add
     * */
    private fun updateChore(position: Int, chore: Chore) {
        chores[position] = chore
        notifyItemChanged(position)
    }

    /**
     * Removes a [Chore] to the list and notifies the Recycler View to update its content
     *
     * @param position: position in which to remove the item
     * */
    private fun removeChore(position: Int) {
        chores.removeAt(position)
        notifyItemRemoved(position)
    }

    /**
     * Removes a certain [Chore] of the list
     *
     * @param position: position in which to remove the item
     * */
    fun removeChoreOnSwap(position: Int) {
        CoroutineScope(Dispatchers.Main).launch {
            chores[position].id?.let {
                SharedPreferences.groupId?.let { id ->
                    val success = ChoreQueries().deleteChore(it, id)
                    if (!success) {
                        Toast.makeText(context, R.string.err_chore_delete, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    /**
     * Marks as completed the selected chore
     *
     * @param position: position in which the completed chore is
     * */
    fun completeChoreOnSwap(position: Int) {
        CoroutineScope(Dispatchers.Main).launch {
            SharedPreferences.groupId?.let { groupId ->
                val success = ChoreQueries().completeChore(chores[position], groupId)
                if (!success) Toast.makeText(context, R.string.err_chore_completion, Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * Inner class that holds the layout for the items in the RecyclerView
     *
     * @param view: current view
     * */
    inner class ChoreHolder(private val view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        private var parent: ConstraintLayout = view.findViewById(R.id.choreListElementParent)
        private var choreIcon: ImageView = view.findViewById(R.id.choreListIcon)
        private var choreTitle: TextView = view.findViewById(R.id.choreListName)
        private var choreAssignee: TextView = view.findViewById(R.id.choreListAssignee)
        private var choreExpiration: TextView = view.findViewById(R.id.choreListExpiration)
        private var choreImportance: View = view.findViewById(R.id.choreListImportance)

        init {
            parent.setOnClickListener(this)
            choreTitle.isSelected = true
        }

        override fun onClick(v: View?) { listener.itemClickInPosition(v, chores[layoutPosition]) }

        /**
         * Initializes the [ChoreHolder] fields with actual data
         *
         * @param position: position on the list
         * */
        fun initializeViews(position: Int) {
            choreTitle.text = chores[position].name
            choreIcon.setImageDrawable(UtilsSingleton.createAvatar(chores[position].name))
            when (chores[position].points) {
                10 -> choreImportance.setBackgroundColor(context.getColor(R.color.importanceLow))
                20 -> choreImportance.setBackgroundColor(context.getColor(R.color.importanceMedium))
                30 -> choreImportance.setBackgroundColor(context.getColor(R.color.importanceHigh))
            }
            chores[position].assignee?.let { uid ->
                if (UtilsSingleton.isUserBeingDisplayedCurrentUser(uid)) {
                    UtilsSingleton.setTextAndIconToYou(context, choreAssignee, null)
                } else {
                    SharedPreferences.groupId?.let { id ->
                        CoroutineScope(Dispatchers.Main).launch {
                            val user = chores[position].assignee?.let {
                                UserQueries().getUser(it, id)
                            }
                            choreAssignee.text = user?.name
                        }
                    }
                }
            }
            val calendar = Calendar.getInstance()
            chores[position].expiration?.let { calendar.time = it }
            choreExpiration.text = UtilsSingleton.parseCalendarToStringOnList(calendar)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChoreHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.chore_list_element, parent, false)
        return ChoreHolder(view)
    }

    override fun onBindViewHolder(holder: ChoreHolder, position: Int) { holder.initializeViews(position) }

    override fun getItemCount(): Int = chores.size
}