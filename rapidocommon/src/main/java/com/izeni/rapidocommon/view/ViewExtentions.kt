@file:Suppress("unused")

package com.izeni.rapidocommon.view

import android.content.res.ColorStateList
import android.graphics.PorterDuff
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.os.Build
import android.support.annotation.StringRes
import android.support.constraint.ConstraintLayout
import android.support.design.widget.TextInputEditText
import android.support.design.widget.TextInputLayout
import android.support.v4.graphics.drawable.DrawableCompat
import android.support.v7.widget.RecyclerView
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import com.izeni.rapidocommon.inputMethodManager

/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 Izeni, Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 **/
data class Biases(val horizontal_bias: Float, val vertical_bias: Float)

fun View.biases(use_center: Boolean = true): Biases {
    val rect = Rect()

    if (use_center) {
        getDrawingRect(rect)
        rect.offset(x.toInt(), y.toInt())
    }

    val _x: Float = if (use_center) rect.exactCenterX() else x
    val _y: Float = if (use_center) rect.exactCenterY() else y

    return Biases(_x / (parent as ViewGroup).width, _y / (parent as ViewGroup).height)

}

fun <T> List<T>.copy() = toList()
fun <T> List<T>.mutableCopy() = toMutableList()

fun View.constraintParams(apply_params: (ConstraintLayout.LayoutParams) -> Unit) {
    lprams(apply_params)
}

fun View.recyclerParams(apply_params: (RecyclerView.LayoutParams) -> Unit) {
    lprams(apply_params)
}

fun View.relativeParams(apply_params: (RelativeLayout.LayoutParams) -> Unit) {
    lprams(apply_params)
}

fun View.linearParams(apply_params: (LinearLayout.LayoutParams) -> Unit) {
    lprams(apply_params)
}

fun View.frameParams(apply_params: (FrameLayout.LayoutParams) -> Unit) {
    lprams(apply_params)
}

fun View.viewGroupParams(apply_params: (ViewGroup.LayoutParams) -> Unit) {
    lprams(apply_params)
}

fun View.onClick(listener: View.OnClickListener) {
    setOnClickListener(listener)
}

fun View.onClick(onClick: (v: View) -> Unit) {
    setOnClickListener(onClick)
}

fun View.setVisible(visible: Boolean, hide: Boolean = true) {
    if (visible) show()
    else if (hide) hide()
    else invisible()
}

fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.GONE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.disable() {
    isEnabled = false
}

fun View.enable() {
    isEnabled = true
}

val View.dm: DisplayMetrics
    get() = resources.displayMetrics

fun View.dpToPx(dp: Int) = (dp * this.dm.density + 0.5).toInt()
fun View.pxToDp(px: Int) = (px / this.dm.density + 0.5).toInt()

fun View.hideSoftKeyboard() {
    context.inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
}

fun EditText.showSoftKeyboard() {
    if (requestFocus()) context.inputMethodManager.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
}

fun EditText.toggleSoftKeyboard() {
    if (requestFocus()) context.inputMethodManager.toggleSoftInput(0, 0)
}

fun ProgressBar.tint(color: Int) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
        indeterminateDrawable = indeterminateDrawable.tint(color)
    } else {
        indeterminateDrawable.setColorFilter(color, PorterDuff.Mode.SRC_IN)
    }
}

inline fun <reified T : ViewGroup.LayoutParams> View.lprams(apply_params: (T) -> Unit) {
    lprams<T>().let { apply_params.invoke(it); this.layoutParams = it }
}

inline fun <reified T : ViewGroup.LayoutParams> View.lprams() = layoutParams as T

fun RecyclerView.ViewHolder.stringRes(@StringRes strRes: Int): String? = itemView.context.getString(strRes)

fun ImageView.tintCurrentDrawable(color: Int) {
    setImageDrawable(drawable.tint(color))
}

fun Drawable.tint(color: Int): Drawable {
    DrawableCompat.wrap(this).let {
        it.mutate()
        DrawableCompat.setTintList(it, ColorStateList.valueOf(color))
        return it
    }
}

fun View.bulkClick(ids: Array<Int>, _onClick: (View) -> Unit) {
    ids.forEach { findViewById(it).apply { onClick { v -> _onClick.invoke(v) } } }
}

fun Array<View>.bulkClick(_onClick: (View) -> Unit) {
    forEach { it.apply { onClick { v -> _onClick.invoke(v) } } }
}

fun EditText.getString(): String = text.toString()
fun TextInputLayout.getString(): String? = editText?.getString()
fun TextInputEditText.getString(): String = text.toString()

fun EditText.isBlank(): Boolean = getString().isNullOrBlank()