package com.gabr.gabc.kelo.rewards

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.gabr.gabc.kelo.R
import com.gabr.gabc.kelo.dataModels.Reward

/** Class that defines the adapter for the recycler view of the periodicity or a reward */
class PeriodicityAdapter(private val context: Context, private val listener: RewardPeriodicityListener)
    : RecyclerView.Adapter<PeriodicityAdapter.PeriodicityHolder>() {

    /**
     * Interface that defines functions to be called by the initializer after meeting certain actions
     * */
    interface RewardPeriodicityListener {
        /**
         * Function that gets the selected periodicity in the adapter to be managed by the caller
         *
         * @param mode: clicked periodicity to be mapped
         * */
        fun onPeriodicityClick(mode: Int)
    }

    /** Class that holds the layout and functionality of each item of the recycler view */
    inner class PeriodicityHolder(inflater: LayoutInflater, parent: ViewGroup)
        : RecyclerView.ViewHolder(inflater.inflate(R.layout.periodicity_bottom_sheet_item, parent, false)),
        View.OnClickListener{
        private val parent: ConstraintLayout = itemView.findViewById(R.id.parentPeriodicityLayout)
        private val periodicity: TextView = itemView.findViewById(R.id.periodicity_item)

        /**
         * Initializes the [PeriodicityHolder] fields with actual data
         *
         * @param position: position on the list
         * */
        fun initializeView(position: Int) {
            parent.setOnClickListener(this)
            periodicity.text = Reward.Frequencies.getStringFromMode(context, position)
        }

        override fun onClick(v: View?) { listener.onPeriodicityClick(layoutPosition) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PeriodicityHolder {
        return PeriodicityHolder(LayoutInflater.from(parent.context), parent)
    }

    override fun onBindViewHolder(holder: PeriodicityHolder, position: Int) { holder.initializeView(position) }

    override fun getItemCount() = Reward.Frequencies.values().size

}