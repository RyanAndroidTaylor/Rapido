package com.izeni.rapidosqlite.util

import android.content.ContentValues
import android.database.Cursor
import com.izeni.rapidosqlite.DataConnection
import com.izeni.rapidosqlite.addAll
import com.izeni.rapidosqlite.get
import com.izeni.rapidosqlite.item_builder.ItemBuilder
import com.izeni.rapidosqlite.table.Column
import com.izeni.rapidosqlite.table.DataTable

data class BadDataTable(val uuid: String, val text: String) : DataTable {

    companion object {
        val TABLE_NAME = "BadDataTable"

        val UUID = Column(String::class.java, "Uuid")
        val TEXT = Column(String::class.java, "Text")

        val COLUMNS = arrayOf(UUID, TEXT)

        val BUILDER = Builder()
    }

    override fun tableName() = TABLE_NAME

    override fun id() = uuid

    override fun idColumn() = UUID

    override fun contentValues() = ContentValues().addAll(COLUMNS, uuid, text)

    class Builder : ItemBuilder<BadDataTable> {
        override fun buildItem(cursor: Cursor, dataConnection: DataConnection): BadDataTable {
            return BadDataTable(cursor.get(UUID), cursor.get(TEXT))
        }
    }
}