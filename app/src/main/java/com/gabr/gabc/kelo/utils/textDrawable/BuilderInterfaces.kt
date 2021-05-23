package com.gabr.gabc.kelo.utils.textDrawable

import android.graphics.Typeface

/**
 * Interfaces that help the TextDrawable view to be constructed upon
 *
 * EXTRACTED FROM https://github.com/amulyakhare/TextDrawable by amulyakhare
 * Translated it to Kotlin as a result of jcenter() going out of service
 * */
class BuilderInterfaces {
    /**
     * Functions to build the TextDrawable View appearance
     * */
    interface IConfigBuilder {
        /**
         * Sets the width of the TextDrawable
         *
         * @param width: to be set width
         * @return itself to keep calling build functions
         * */
        fun width(width: Int): IConfigBuilder?
        /**
         * Sets the height of the TextDrawable
         *
         * @param height: to be set height
         * @return itself to keep calling build functions
         * */
        fun height(height: Int): IConfigBuilder?
        /**
         * Sets the text color of the TextDrawable
         *
         * @param color: to be set color
         * @return itself to keep calling build functions
         * */
        fun textColor(color: Int): IConfigBuilder?
        /**
         * Sets the text font of the TextDrawable
         *
         * @param font: to be set font
         * @return itself to keep calling build functions
         * */
        fun useFont(font: Typeface?): IConfigBuilder?
        /**
         * Sets the text font size of the TextDrawable
         *
         * @param size: to be set size
         * @return itself to keep calling build functions
         * */
        fun fontSize(size: Int): IConfigBuilder?
        /**
         * Sets whether the text is bold or not for the TextDrawable
         *
         * @return itself to keep calling build functions
         * */
        fun bold(): IConfigBuilder?
        /**
         * Sets whether the text should be uppercase or not for the TextDrawable
         *
         * @return itself to keep calling build functions
         * */
        fun toUpperCase(): IConfigBuilder?
        /**
         * Ends the configuration of the TextDrawable
         *
         * @return the actual build drawable to paint it in the UI
         * */
        fun endConfig(): IShapeBuilder?
    }

    /**
     * Functions to set the initial parameters of the TextDrawable View
     * */
    interface IBuilder {
        /**
         * Sets the actual text and color of the TextDrawable View
         *
         * @param text: text that should appear in the center of the view. Only initial
         * @param color: random material color for the background of the view
         * @return Drawable extension to paint in the UI with the [IConfigBuilder] set
         * */
        fun build(text: String?, color: Long): TextDrawable?
    }

    /**
     * Functions to set the shape of the TextDrawable View
     * */
    interface IShapeBuilder {
        /**
         * Initializes the configuration of the View
         *
         * @return the configuration builder [IConfigBuilder]
         * */
        fun beginConfig(): IConfigBuilder?
        /**
         * Defines the TextDrawable View as a round drawable
         *
         * @return [IBuilder] to set the text and the color of it
         * */
        fun round(): IBuilder?
        /**
         * Defines the TextDrawable View as a circled drawable without going through [IBuilder]
         * setting already the parameters
         *
         * @param text: text that should appear in the center of the view. Only initial
         * @param color: random material color for the background of the view
         * @return Drawable extension to paint in the UI with the [IConfigBuilder] set
         * */
        fun buildRound(text: String?, color: Long): TextDrawable?
    }
}