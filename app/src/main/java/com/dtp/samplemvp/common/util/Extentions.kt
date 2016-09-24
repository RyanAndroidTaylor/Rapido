package com.dtp.samplemvp.common.util

import android.database.Cursor

/**
 * Created by ryantaylor on 9/23/16.
 */

fun Cursor.getString(columnName: String): String {
    return getString(getColumnIndex(columnName))
}

fun Cursor.getInt(columnName: String): Int {
    return getInt(getColumnIndex(columnName))
}

fun Cursor.getLong(columnName: String): Long {
    return getLong(getColumnIndex(columnName))
}

fun Cursor.getBoolean(columnName: String): Boolean {
    return getInt(getColumnIndex(columnName)) == 1
}