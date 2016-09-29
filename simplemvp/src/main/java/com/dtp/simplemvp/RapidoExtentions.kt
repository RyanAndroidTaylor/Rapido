package com.dtp.simplemvp

import android.content.ContentValues
import android.database.Cursor
import com.dtp.simplemvp.database.table.Column

/**
 * Created by ryantaylor on 9/23/16.
 */

fun <T> Cursor.get(column: Column): T {
    when (column.type) {
        is String -> return getString(getColumnIndex(column.name)) as T
        is Int -> return getInt(getColumnIndex(column.name)) as T
        is Long -> return getLong(getColumnIndex(column.name)) as T
        else -> throw UnsupportedOperationException("${column.type} is not a supported type")
    }
}

/**
 * Returns content values where the values are stored base on the colulmns name.
 * The columns and values must be in the same order.
 */
fun ContentValues.addAll(columns: Array<Column>, values: Array<Any>): ContentValues {
    for ((index, column) in columns.withIndex()) {
        val value = values[index]
        when (column.type) {
            is String -> put(column.name, value as String)
            is Int -> put(column.name, value as Int)
            is Long -> put(column.name, value as Long)
            is Boolean -> put(column.name, value as Boolean)
            else -> throw IllegalArgumentException("${column.type} not supported by this method")
        }
    }

    return this
}