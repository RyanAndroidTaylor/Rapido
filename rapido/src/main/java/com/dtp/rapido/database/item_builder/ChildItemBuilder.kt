package com.dtp.rapido.database.item_builder

import com.dtp.rapido.database.table.ChildDataTable
import com.dtp.rapido.database.table.Column

/**
 * Created by ryantaylor on 9/24/16.
 */
interface ChildItemBuilder<out T: ChildDataTable>: ItemBuilder<T> {
    val tableName: String
    val foreignKey: Column
}