package com.gabr.gabc.kelo.utils.textDrawable

import kotlin.collections.ArrayList
import kotlin.math.abs

/**
 * Color generator. This class has a list of Material colors and user can select any at random
 * to be populated in the TextDrawable View.
 *
 * EXTRACTED FROM https://github.com/amulyakhare/TextDrawable by amulyakhare
 * Translated it to Kotlin as a result of jcenter() going out of service
 * */
class ColorGenerator(private val colors: ArrayList<Long>) {
    companion object {
        val MATERIAL = arrayListOf(
            0xffe57373, 0xfff06292, 0xffba68c8, 0xff9575cd,
            0xff7986cb, 0xff64b5f6, 0xff4fc3f7, 0xff4dd0e1,
            0xff4db6ac, 0xff81c784, 0xffaed581, 0xffff8a65,
            0xffd4e157, 0xffffd54f, 0xffffb74d, 0xffa1887f,
            0xff90a4ae
        )
    }

    /**
     * Function that creates an instance of [ColorGenerator] with a given color list
     *
     * @param colorList: color list to be initialized with
     * */
    fun create(colorList: ArrayList<Long>): ColorGenerator { return ColorGenerator(colorList) }
    /**
     * Function that selects a random color from [MATERIAL] based on a key. This key is mapped
     * in order to get the same color for the same key in every instance
     *
     * @param key: text of the TextDrawable
     * */
    fun getColor(key: Any?): Long = colors[abs(key.hashCode()) % colors.size]
}