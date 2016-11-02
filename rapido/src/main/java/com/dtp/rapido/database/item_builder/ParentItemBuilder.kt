package com.dtp.rapido.database.item_builder

import android.database.Cursor
import com.dtp.rapido.database.table.ChildDataTable
import com.dtp.rapido.database.table.Column

/**
 * Created by ryantaylor on 9/24/16.
 */
abstract class ParentItemBuilder<out T> : ItemBuilder<T> {
    abstract val foreignKey: Column

    override fun buildItem(cursor: Cursor): T {
        throw UnsupportedOperationException("Parent must use buildItem(cursor: Cursor, children: List<ChildDataTable>? = null): T method")
    }

    abstract fun buildItem(cursor: Cursor, children: List<ChildDataTable>? = null): T
    abstract fun getChildBuilders(): List<ChildItemBuilder<*>>
}