package com.gabr.gabc.kelo.utils.textDrawable

import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.shapes.OvalShape
import android.graphics.drawable.shapes.RectShape

/**
 * EXTRACTED FROM https://github.com/amulyakhare/TextDrawable by amulyakhare
 *
 * Translated it to Kotlin as a result of jcenter() going out of service
 * */
class Builder : BuilderInterfaces.IConfigBuilder, BuilderInterfaces.IShapeBuilder, BuilderInterfaces.IBuilder {
    var text = ""
    var color: Long? = null
    var width: Int? = null
    var height: Int? = null
    var font: Typeface
    var shape: RectShape
    var textColor: Int? = null
    var fontSize: Int? = null
    var isBold: Boolean? = null
    var toUpperCase: Boolean? = null

    init {
        color = Color.GRAY.toLong()
        textColor = Color.WHITE
        width = -1
        height = -1
        shape = RectShape()
        font = Typeface.create("sans-serif-light", Typeface.NORMAL)
        fontSize = -1
        isBold = false
        toUpperCase = false
    }

    override fun beginConfig(): BuilderInterfaces.IConfigBuilder = this
    override fun endConfig(): BuilderInterfaces.IShapeBuilder = this

    override fun build(text: String?, color: Long): TextDrawable {
        this.color = color
        text?.let { this.text = it }
        return TextDrawable(this)
    }

    override fun round(): BuilderInterfaces.IBuilder {
        shape = OvalShape()
        return this
    }

    override fun buildRound(text: String?, color: Long): TextDrawable {
        round()
        return build(text, color)
    }

    override fun width(width: Int): BuilderInterfaces.IConfigBuilder {
        this.width = width
        return this
    }

    override fun height(height: Int): BuilderInterfaces.IConfigBuilder {
        this.height = height
        return this
    }

    override fun textColor(color: Int): BuilderInterfaces.IConfigBuilder {
        textColor = color
        return this
    }

    override fun useFont(font: Typeface?): BuilderInterfaces.IConfigBuilder {
        font?.let { this.font = it }
        return this
    }

    override fun fontSize(size: Int): BuilderInterfaces.IConfigBuilder {
        fontSize = size
        return this
    }

    override fun bold(): BuilderInterfaces.IConfigBuilder {
        isBold = true
        return this
    }

    override fun toUpperCase(): BuilderInterfaces.IConfigBuilder {
        toUpperCase = true
        return this
    }
}