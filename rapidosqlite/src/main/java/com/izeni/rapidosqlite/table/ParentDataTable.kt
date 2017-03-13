package com.izeni.rapidosqlite.table

/**
 * Created by ryantaylor on 9/23/16.
 */
interface ParentDataTable : DataTable {
    fun getChildren(): List<DataTable>
}