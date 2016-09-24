package com.dtp.simplemvp.database.item_builder

import android.database.Cursor
import com.dtp.simplemvp.database.item_builder.ChildItemBuilder
import com.dtp.simplemvp.database.item_builder.ItemBuilder
import com.dtp.simplemvp.database.table.ChildDataTable

/**
 * Created by ryantaylor on 9/24/16.
 */
abstract class ParentItemBuilder<T> : ItemBuilder<T> {
    override fun buildItem(cursor: Cursor): T {
        throw UnsupportedOperationException("Parent must use buildItem(cursor: Cursor, children: List<ChildDataTable>? = null): T build method")
    }

    abstract fun buildItem(cursor: Cursor, children: List<ChildDataTable>? = null): T
    abstract fun getChildBuilders(): List<Pair<String, ChildItemBuilder>>
}