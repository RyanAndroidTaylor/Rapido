package com.dtp.simplemvp.database

/**
 * Created by ryantaylor on 9/23/16.
 */
interface ParentDataTable : DataTable {
    fun getChildren(): List<ChildDataTable>
}