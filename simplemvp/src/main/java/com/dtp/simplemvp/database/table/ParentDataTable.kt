package com.dtp.simplemvp.database.table

import com.dtp.simplemvp.database.table.DataTable
import com.dtp.simplemvp.database.table.ChildDataTable

/**
 * Created by ryantaylor on 9/23/16.
 */
interface ParentDataTable : DataTable {
    fun getChildren(): List<ChildDataTable>
}