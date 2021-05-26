package com.gabr.gabc.kelo.welcome.viewBottomSheet

import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import com.gabr.gabc.kelo.R
import com.gabr.gabc.kelo.constants.Constants
import com.gabr.gabc.kelo.welcome.WelcomeViewModel

/** Bottom Sheet Dialog Fragment that holds the currency list in the WelcomeActivity */
class CurrencyBottomSheet : BottomSheetDialogFragment() {

    companion object {
        const val TAG = "currency_bottom_sheet"
    }

    private lateinit var currencyList: RecyclerView

    private lateinit var viewModel: WelcomeViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.currency_bottom_sheet, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = activity?.run { ViewModelProvider(this).get(WelcomeViewModel::class.java) }!!
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        currencyList = view.findViewById(R.id.currencyList)
        currencyList.layoutManager = LinearLayoutManager(context)
        currencyList.adapter = CurrencyAdapter(Constants.CURRENCIES.sortedBy { it.name })
    }

    /**
     * Adapter for the Recycler View.
     * It creates the [CurrencyItem] for every position and attaches a listener for each item
     * for updating the [WelcomeViewModel] - the variable groupCurrency
     * */
    private inner class CurrencyAdapter(val currencies: List<CurrencyModel>): RecyclerView.Adapter<CurrencyAdapter.CurrencyItem>() {
        inner class CurrencyItem(inflater: LayoutInflater, parent: ViewGroup)
            : RecyclerView.ViewHolder(inflater.inflate(R.layout.currency_bottom_sheet_item, parent, false)) {
            private val item: ConstraintLayout = itemView.findViewById(R.id.currencyTab)
            private val label: TextView = itemView.findViewById(R.id.currencyLabel)
            private val flag: ImageView = itemView.findViewById(R.id.currencyFlag)

            /**
             * Initializes the view of the [CurrencyItem] at a given position
             *
             * @param position: position of the list to be initialized
             * */
            fun initializeView(position: Int) {
                label.text = currencies[position].name
                flag.setImageResource(currencies[position].flag)
                item.setOnClickListener {
                    viewModel.setCurrency(currencies[position])
                    dismiss()
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyItem {
            return CurrencyItem(LayoutInflater.from(parent.context), parent)
        }

        override fun onBindViewHolder(holder: CurrencyItem, position: Int) { holder.initializeView(position) }

        override fun getItemCount(): Int { return currencies.size }
    }
}