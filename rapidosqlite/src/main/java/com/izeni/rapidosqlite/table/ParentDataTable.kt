package com.izeni.rapidosqlite.table

/**
 * Created by ryantaylor on 9/23/16.
 */
interface ParentDataTable : DataTable {
    fun getJunctionTables(): List<JunctionDataTable>? = null
    fun getChildren(): List<ChildDataTable>? = null
}