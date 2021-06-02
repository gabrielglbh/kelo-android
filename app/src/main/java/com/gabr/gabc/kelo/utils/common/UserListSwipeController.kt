package com.gabr.gabc.kelo.utils.common

import android.content.Context
import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.gabr.gabc.kelo.R
import com.gabr.gabc.kelo.utils.UtilsSingleton

/**
 * Controller for the swipe of the lists. It makes the recycler view elements able to be swapped
 * left or right for further actions in the list as removal of elements.
 *
 * @param dragDirs: direction of the drag. Usually 0
 * @param swipeDirs: swap direction. Usually LEFT and RIGHT
 * @param adapter: RecyclerView adapter for the onSwiped function action
 * @param context: activity or fragment context
 * */
class UserListSwipeController(
    dragDirs: Int, swipeDirs: Int, private val adapter: UsersAdapter, private val context: Context) :
    ItemTouchHelper.SimpleCallback(dragDirs, swipeDirs) {

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
                        target: RecyclerView.ViewHolder): Boolean = false

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        adapter.removeUserFromGroupOnSwap(viewHolder.layoutPosition)
    }

    override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
        dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        lateinit var icon: Drawable
        val itemView: View = viewHolder.itemView

        val background = ColorDrawable(context.getColor(R.color.errorColor))
        ContextCompat.getDrawable(context, R.drawable.clear)?.let { icon = it }

        UtilsSingleton.setUpSwipeController(c, context, dX, itemView, icon, background)
    }
}