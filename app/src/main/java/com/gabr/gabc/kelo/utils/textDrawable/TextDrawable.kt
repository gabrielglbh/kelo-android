package com.gabr.gabc.kelo.utils.textDrawable

import android.graphics.*
import android.graphics.drawable.ShapeDrawable
import java.util.*

/**
 * EXTRACTED FROM https://github.com/amulyakhare/TextDrawable by amulyakhare
 *
 * Translated it to Kotlin as a result of jcenter() going out of service
 * */
class TextDrawable(private val builder: Builder) : ShapeDrawable() {
    private var textPaint: Paint? = null
    private var text: String? = null
    private var color: Long = 0
    private var height = 0
    private var width = 0
    private var fontSize = 0

    companion object {
        fun builder(): BuilderInterfaces.IShapeBuilder = Builder()
    }

    init {
        // Variable shape of ShapeDrawable
        shape = builder.shape
        builder.height?.let { height = it }
        builder.width?.let { width = it }

        // text and color
        builder.toUpperCase?.let { text = if (it) builder.text.toUpperCase(Locale.ROOT) else builder.text }
        builder.color?.let { color = it }

        // text paint settings
        builder.fontSize?.let { fontSize = it }
        textPaint = Paint()
        textPaint?.let {
            builder.textColor?.let { c -> it.color = c }
            it.isAntiAlias = true
            builder.isBold?.let { b -> it.isFakeBoldText = b }
            it.style = Paint.Style.FILL
            it.typeface = builder.font
            it.textAlign = Paint.Align.CENTER
        }

        // drawable paint color
        val paint = paint
        paint.color = color.toInt()
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        val r: Rect = bounds

        val count = canvas.save()
        canvas.translate(r.left.toFloat(), r.top.toFloat())

        // draw text
        val width = if (width < 0) r.width() else width
        val height = if (height < 0) r.height() else height
        val fontSize = if (fontSize < 0) width.coerceAtMost(height) / 2 else fontSize
        textPaint!!.textSize = fontSize.toFloat()
        canvas.drawText(
            text!!,
            (width / 2).toFloat(), height / 2 - (textPaint!!.descent() + textPaint!!.ascent()) / 2,
            textPaint!!
        )

        canvas.restoreToCount(count)
    }

    override fun setAlpha(alpha: Int) { textPaint!!.alpha = alpha }
    override fun setColorFilter(cf: ColorFilter?) { textPaint!!.colorFilter = cf }
    override fun getOpacity(): Int = PixelFormat.TRANSLUCENT
    override fun getIntrinsicWidth(): Int = width
    override fun getIntrinsicHeight(): Int = height
}