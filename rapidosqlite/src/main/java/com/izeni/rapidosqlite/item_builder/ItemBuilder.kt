package com.izeni.rapidosqlite.item_builder

import android.database.Cursor
import com.izeni.rapidosqlite.DataConnection

/**
 * Created by ryantaylor on 9/23/16.
 */
interface ItemBuilder<out T> {
    fun buildItem(cursor: Cursor, dataConnection: DataConnection): T
}