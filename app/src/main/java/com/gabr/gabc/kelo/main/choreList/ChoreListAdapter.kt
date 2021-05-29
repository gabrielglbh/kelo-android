package com.gabr.gabc.kelo.main.choreList

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
import com.gabr.gabc.kelo.utils.*
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
class ChoreListAdapter(private val listener: ChoreClickListener, private val context: Context,
                       loading: ProgressBar, groupId: String?)
    : RecyclerView.Adapter<ChoreListAdapter.ChoreHolder>() {

    /**
     * Interface that defines a function to be called by the initializer when clicking on a certain item
     * */
    interface ChoreClickListener {
        /**
         * Function that gets the selected [Chore] in the adapter to be managed by the caller
         *
         * @param chore: clicked [Chore]
         * */
        fun onChoreClick(chore: Chore)
    }
    var chores: ArrayList<Chore> = arrayListOf()

    /**
     * Initializes the listener to the chores list in Firebase. It updates
     * the list every time the collection changes
     * */
    init {
        if (groupId != null) {
            LoadingSingleton.manageLoadingView(loading, null, true)
            val listener = ChoreQueries().attachListenerToChores(groupId,
                { position, chore -> addChoreAtPosition(position, chore) },
                { position, chore -> updateChoreAtPosition(position, chore) },
                { position -> removeChoreAtPosition(position) })

            if (listener == null) Toast.makeText(context, R.string.err_loading_chores, Toast.LENGTH_SHORT).show()
            LoadingSingleton.manageLoadingView(loading, null, false)
        } else Toast.makeText(context, R.string.err_group_does_not_exist, Toast.LENGTH_SHORT).show()
    }

    private fun addChoreAtPosition(position: Int, chore: Chore) {
        chores.add(position, chore)
        notifyItemInserted(position)
    }

    private fun updateChoreAtPosition(position: Int, chore: Chore) {
        chores[position] = chore
        notifyItemChanged(position)
    }

    private fun removeChoreAtPosition(position: Int) {
        chores.removeAt(position)
        notifyItemRemoved(position)
    }

    /**
     * Removes a certain [Chore] of the list. If it fails somehow, [notifyItemChanged] is called
     * to make the view return to its initial position. Only the creator of the chore can remove it.
     *
     * @param position: position in which to remove the item
     * */
    fun removeChoreOnSwap(position: Int) {
        SharedPreferences.groupId?.let { gid ->
            SharedPreferences.userId?.let { uid ->
                CoroutineScope(Dispatchers.Main).launch {
                    val user = UserQueries().getUser(uid, gid)
                    val chore = chores[position]
                    chore.id?.let { choreId ->
                        chore.creator?.let { creator ->
                            if (PermissionsSingleton.isUserChoreCreator(creator) || PermissionsSingleton.isUserAdmin(user)) {
                                val success = ChoreQueries().deleteChore(choreId, gid)
                                if (!success) {
                                    Toast.makeText(context, R.string.err_chore_delete, Toast.LENGTH_SHORT).show()
                                }
                            } else {
                                Toast.makeText(context, context.getString(R.string.permission_remove_chore), Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }
        }
        notifyItemChanged(position)
    }

    /**
     * Marks as completed the selected chore. If it fails somehow, [notifyItemChanged] is called
     * to make the view return to its initial position. Only the creator of the chore can complete it.
     *
     * @param position: position in which the completed chore is
     * */
    fun completeChoreOnSwap(position: Int) {
        SharedPreferences.groupId?.let { gid ->
            SharedPreferences.userId?.let { uid ->
                CoroutineScope(Dispatchers.Main).launch {
                    val user = UserQueries().getUser(uid, gid)
                    val chore = chores[position]
                    chore.assignee?.let { assignee ->
                        chore.creator?.let { creator ->
                            if (PermissionsSingleton.isUserChoreCreatorOrAssignee(creator, assignee)
                                || PermissionsSingleton.isUserAdmin(user)) {
                                val success = ChoreQueries().completeChore(chore, gid)
                                if (!success) Toast.makeText(context, R.string.err_chore_completion, Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(context, context.getString(R.string.permission_complete_chore), Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }
        }
        notifyItemChanged(position)
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
        private var choreCreator: TextView = view.findViewById(R.id.choreListCreator)
        private var choreExpiration: TextView = view.findViewById(R.id.choreListExpiration)
        private var choreImportance: View = view.findViewById(R.id.choreListImportance)

        init {
            parent.setOnClickListener(this)
            choreTitle.isSelected = true
        }

        override fun onClick(v: View?) { listener.onChoreClick(chores[layoutPosition]) }

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
                SharedPreferences.groupId?.let { id ->
                    getUserForTextView(choreAssignee, uid, id)
                }
            }
            chores[position].creator?.let { uid ->
                SharedPreferences.groupId?.let { id ->
                    getUserForTextView(choreCreator, uid, id)
                }
            }
            val calendar = Calendar.getInstance()
            chores[position].expiration?.let { calendar.time = it }
            choreExpiration.text = DatesSingleton.parseCalendarToStringOnList(calendar)
        }

        private fun getUserForTextView(textView: TextView, uid: String, groupId: String) {
            CoroutineScope(Dispatchers.Main).launch {
                val user = UserQueries().getUser(uid, groupId)
                if (SharedPreferences.isUserBeingDisplayedCurrentUser(uid)) {
                    if (user != null) textView.text = UtilsSingleton.setTextForCurrentUser(context, user.name)
                    else textView.text = context.getString(R.string.chore_not_assigned)
                } else {
                    if (user != null) textView.text = user.name
                    else textView.text = context.getString(R.string.chore_not_assigned)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChoreHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.chore_list_element, parent, false)
        return ChoreHolder(view)
    }

    override fun onBindViewHolder(holder: ChoreHolder, position: Int) { holder.initializeViews(position) }

    override fun getItemCount(): Int = chores.size
}