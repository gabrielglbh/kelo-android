package com.gabr.gabc.kelo.welcomeActivity.viewBottomSheet

class CurrencyModel(private val code: String, private val name: String,
                    private val symbol: String, private val flag: Int) {
    fun getCode(): String = code
    fun getFlag(): Int = flag
    fun getName(): String = name
    fun getSymbol(): String = symbol
}