package com.dtp.rapido.database.table

/**
 * Created by ryantaylor on 9/23/16.
 */
interface ParentDataTable : DataTable {
    fun getChildren(): List<ChildDataTable>
    fun getForeignKeyValue(): String
}