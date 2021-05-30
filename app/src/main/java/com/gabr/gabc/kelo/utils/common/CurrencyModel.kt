package com.gabr.gabc.kelo.utils.common

/**
 * Model that holds the currency object
 *
 * @param code: ISO code
 * @param name: Currency name
 * @param symbol: Currency symbol
 * @param flag: Currency's country flag
 * */
class CurrencyModel(val code: String, val name: String, private val symbol: String, val flag: Int)