@file:Suppress("unused")
package com.izeni.rapidocommon.view

import android.app.Activity
import android.app.Dialog
import android.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.View
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

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
inline fun <reified V : View> View.find(id: Int): V = findViewById(id) as V

inline fun <reified V : View> Activity.find(id: Int): V = findViewById(id) as V
inline fun <reified V : View> Dialog.find(id: Int): V = findViewById(id) as V
inline fun <reified V : View> android.support.v4.app.Fragment.find(id: Int): V = view!!.findViewById(id) as V
inline fun <reified V : View> Fragment.find(id: Int): V = view!!.findViewById(id) as V
inline fun <reified V : View> RecyclerView.ViewHolder.find(id: Int): V = itemView.findViewById(id) as V

fun <V : View> View.bind(id: Int): ReadOnlyProperty<View, V> = required(id, { find(it) })
fun <V : View> Activity.bind(id: Int): ReadOnlyProperty<Activity, V> = required(id, { find(it) })
fun <V : View> Dialog.bind(id: Int): ReadOnlyProperty<Dialog, V> = required(id, { find(it) })
fun <V : View> Fragment.bind(id: Int): ReadOnlyProperty<Fragment, V> = required(id, { find(it) })
fun <V : View> android.support.v4.app.Fragment.bind(id: Int): ReadOnlyProperty<android.support.v4.app.Fragment, V> = required(id, { find(it) })
fun <V : View> RecyclerView.ViewHolder.bind(id: Int): ReadOnlyProperty<RecyclerView.ViewHolder, V> = required(id, { find(it) })

private fun viewNotFound(id: Int, desc: KProperty<*>): Nothing = throw IllegalStateException("View ID $id for '${desc.name}' not found.")

@Suppress("UNCHECKED_CAST")
private fun <T, V : View> required(id: Int, finder: T.(Int) -> View?) = ViewLazy { t: T, desc -> t.finder(id) as V? ?: viewNotFound(id, desc) }

private class ViewLazy<in T, out V>(private val initializer: (T, KProperty<*>) -> V) : ReadOnlyProperty<T, V> {
    private object EMPTY

    private var value: Any? = EMPTY

    override fun getValue(thisRef: T, property: KProperty<*>): V {
        if (value == EMPTY) {
            value = initializer(thisRef, property)
        }
        @Suppress("UNCHECKED_CAST")
        return value as V
    }
}