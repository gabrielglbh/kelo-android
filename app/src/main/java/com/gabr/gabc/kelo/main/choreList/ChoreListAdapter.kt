package com.gabr.gabc.kelo.main.choreList

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.gabr.gabc.kelo.R
import com.gabr.gabc.kelo.firebase.ChoreQueries
import com.gabr.gabc.kelo.firebase.UserQueries
import com.gabr.gabc.kelo.dataModels.Chore
import com.gabr.gabc.kelo.utils.*
import com.gabr.gabc.kelo.viewModels.ChoreListViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

/**
 * Adapter for the Chore List.
 *
 * @param lifecycleOwner: owner of the observable of the live data object
 * @param viewModel: [ChoreListViewModel] to observe the state of the local chore list
 * @param context: activity's context
 * @param parent: view in which to show the snack bar
 * @param anchor: view to set the snack bar above it
 * */
class ChoreListAdapter(lifecycleOwner: LifecycleOwner,
                       viewModel: ChoreListViewModel,
                       private val listener: ChoreListener,
                       private val context: Context,
                       private val parent: View,
                       private val anchor: View)
    : RecyclerView.Adapter<ChoreListAdapter.ChoreHolder>() {

    private val chores = arrayListOf<Chore>()

    init {
        viewModel.choreList.observe(lifecycleOwner, { chores ->
            this.chores.clear()
            this.chores.addAll(chores)
            notifyDataSetChanged()
        })
    }

    /**
     * Interface that defines functions to be called by the initializer after meeting certain actions
     * */
    interface ChoreListener {
        /**
         * Function that gets the selected [Chore] in the adapter to be managed by the caller
         *
         * @param chore: clicked [Chore]
         * */
        fun onChoreClick(chore: Chore)
    }

    private fun removedAt(position: Int) {
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
        val gid = SharedPreferences.groupId
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val user = UserQueries().getUser(SharedPreferences.userId, gid)
                if (user != null) {
                    val chore = chores[position]
                    if (PermissionsSingleton.isUserChoreCreator(chore.creator!!) || PermissionsSingleton.isUserAdmin(user)) {
                        val success = ChoreQueries().deleteChore(chore.id!!, gid)
                        if (success) removedAt(position)
                        else {
                            UtilsSingleton.showSnackBar(parent, context.getString(R.string.err_chore_delete),
                                anchorView = anchor)
                        }
                    } else {
                        UtilsSingleton.showSnackBar(parent, context.getString(R.string.permission_remove_chore),
                            anchorView = anchor)
                    }
                }
            } catch (e: Exception) {
                UtilsSingleton.showSnackBar(parent, context.getString(R.string.err_chore_delete),
                    anchorView = anchor)
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
        val gid = SharedPreferences.groupId
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val user = UserQueries().getUser(SharedPreferences.userId, gid)
                if (user != null) {
                    val chore = chores[position]
                    if (PermissionsSingleton.isUserChoreCreatorOrAssignee(chore.creator!!, chore.assignee!!)
                        || PermissionsSingleton.isUserAdmin(user)) {
                        val success = ChoreQueries().completeChore(chore, gid)
                        if (success) removedAt(position)
                        else {
                            UtilsSingleton.showSnackBar(parent, context.getString(R.string.err_chore_completion),
                                anchorView = anchor)
                        }
                    } else {
                        UtilsSingleton.showSnackBar(parent, context.getString(R.string.permission_complete_chore),
                            anchorView = anchor)
                    }
                }
            } catch (e: Exception) {
                UtilsSingleton.showSnackBar(parent, context.getString(R.string.err_chore_completion),
                    anchorView = anchor)
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
            choreAssignee.isSelected = true
            choreCreator.isSelected = true
        }

        override fun onClick(v: View?) { listener.onChoreClick(chores[layoutPosition]) }

        /**
         * Initializes the [ChoreHolder] fields with actual data
         *
         * @param position: position on the list
         * */
        fun initializeViews(position: Int) {
            val gid = SharedPreferences.groupId
            choreTitle.text = chores[position].name
            choreIcon.setImageDrawable(UtilsSingleton.createAvatar(chores[position].name))
            when (chores[position].points) {
                10 -> choreImportance.setBackgroundColor(context.getColor(R.color.importanceLow))
                20 -> choreImportance.setBackgroundColor(context.getColor(R.color.importanceMedium))
                30 -> choreImportance.setBackgroundColor(context.getColor(R.color.importanceHigh))
            }
            chores[position].assignee?.let { uid ->
                getUserForTextView(choreAssignee, uid, gid)
            }
            chores[position].creator?.let { uid ->
                getUserForTextView(choreCreator, uid, gid)
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