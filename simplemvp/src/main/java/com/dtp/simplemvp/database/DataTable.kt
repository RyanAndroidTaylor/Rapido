package com.dtp.simplemvp.database

import android.content.ContentValues

/**
 * Created by ryantaylor on 9/22/16.
 */
interface DataTable {
    fun tableName(): String
    fun contentValues(): ContentValues
}