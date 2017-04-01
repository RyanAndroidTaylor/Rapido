package com.izeni.rapidosqlite.table

import android.content.ContentValues
import com.izeni.rapidosqlite.item_builder.ItemBuilder

/**
 * Created by ryantaylor on 9/22/16.
 */
interface DataTable {
    fun id(): String
    fun idColumn(): Column
    fun tableName(): String
    fun contentValues(): ContentValues
}