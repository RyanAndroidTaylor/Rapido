@file:Suppress("unused")
package com.izeni.rapidocommon

import android.database.Cursor
import android.os.Bundle

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
fun Cursor.index(column: String, throw_error: Boolean = false): Int? {
    try {
        return getColumnIndexOrThrow(column)
    } catch (e: IllegalArgumentException) {
        e("COLUMN ERROR:", t = e)
        if (throw_error) throw e
        return null
    }
}

fun Cursor.asBlob(column: String, throw_error: Boolean = false): ByteArray? {
    index(column, throw_error)?.let {
        return getBlob(it)
    }
    return null
}

fun Cursor.asLong(column: String, throw_error: Boolean = false): Long? {
    index(column, throw_error)?.let {
        return getLong(it)
    }
    return null
}

fun Cursor.asDouble(column: String, throw_error: Boolean = false): Double? {
    index(column, throw_error)?.let {
        return getDouble(it)
    }
    return null
}

fun Cursor.asBundle(): Bundle? {
    return extras
}

fun Cursor.asFloat(column: String, throw_error: Boolean = false): Float? {
    index(column, throw_error)?.let {
        return getFloat(it)
    }
    return null
}

fun Cursor.asInt(column: String, throw_error: Boolean = false): Int? {
    index(column, throw_error)?.let {
        return getInt(it)
    }
    return null
}

fun Cursor.getLong(column: String, throw_error: Boolean = false): Long? {
    index(column, throw_error)?.let {
        return getLong(it)
    }
    return null
}

fun Cursor.asShort(column: String, throw_error: Boolean = false): Short? {
    index(column, throw_error)?.let {
        return getShort(it)
    }
    return null
}

fun Cursor.asString(column: String, throw_error: Boolean = false): String? {
    index(column, throw_error)?.let {
        return getString(it)
    }
    return null
}