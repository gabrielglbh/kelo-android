package com.gabr.gabc.kelo.utils.textDrawable

import android.graphics.Typeface

/**
 * EXTRACTED FROM https://github.com/amulyakhare/TextDrawable by amulyakhare
 *
 * Translated it to Kotlin as a result of jcenter() going out of service
 * */
class BuilderInterfaces {
    interface IConfigBuilder {
        fun width(width: Int): IConfigBuilder?
        fun height(height: Int): IConfigBuilder?
        fun textColor(color: Int): IConfigBuilder?
        fun useFont(font: Typeface?): IConfigBuilder?
        fun fontSize(size: Int): IConfigBuilder?
        fun bold(): IConfigBuilder?
        fun toUpperCase(): IConfigBuilder?
        fun endConfig(): IShapeBuilder?
    }

    interface IBuilder {
        fun build(text: String?, color: Long): TextDrawable?
    }

    interface IShapeBuilder {
        fun beginConfig(): IConfigBuilder?
        fun round(): IBuilder?
        fun buildRound(text: String?, color: Long): TextDrawable?
    }
}