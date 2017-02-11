package com.izeni.rapidosqlite

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.izeni.rapidosqlite.table.Column

/**
 * Created by ryantaylor on 9/23/16.
 */

// This is not safe if you are using it with a table that was created some other way than using the TableBuilder class
@Suppress("UNCHECKED_CAST")
fun <T> Cursor.get(column: Column): T {
    when (column.type) {
        is String -> return getString(getColumnIndex(column.name)) as T
        is Int -> return getInt(getColumnIndex(column.name)) as T
        is Long -> return getLong(getColumnIndex(column.name)) as T
        is Boolean -> return (getInt(getColumnIndex(column.name)) == 1) as T
        else -> throw UnsupportedOperationException("${column.type} is not a supported type")
    }
}

/**
 * Returns content values where the values are stored base on the colulmns name.
 * The columns and values must be in the same order.
 */
fun ContentValues.addAll(columns: Array<Column>, vararg values: Any?): ContentValues {
    for ((index, column) in columns.withIndex()) {
        val value = values[index]
        if (value != null) {
            when (column.type) {
                is String -> put(column.name, value as String)
                is Int -> put(column.name, value as Int)
                is Long -> put(column.name, value as Long)
                is Boolean -> put(column.name, if ((value as Boolean)) 1 else 0)
                else -> throw IllegalArgumentException("${column.type} not supported by this method")
            }
        }
    }

    return this
}

inline fun SQLiteDatabase.transaction(block: (SQLiteDatabase) -> Unit) {
    beginTransaction()
    try {
        block(this)

        setTransactionSuccessful()
    } catch (e: Exception) {
        Log.e("DatabaseExtension", e.message)
    } finally {
        endTransaction()
    }
}