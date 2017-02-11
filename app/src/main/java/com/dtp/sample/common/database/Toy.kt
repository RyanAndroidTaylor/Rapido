package com.dtp.sample.common.database

import android.content.ContentValues
import android.database.Cursor
import com.izeni.rapidosqlite.DataConnection
import com.izeni.rapidosqlite.addAll
import com.izeni.rapidosqlite.get
import com.izeni.rapidosqlite.item_builder.ItemBuilder
import com.izeni.rapidosqlite.table.ChildDataTable
import com.izeni.rapidosqlite.table.Column
import com.izeni.rapidosqlite.table.Column.Companion.LONG
import com.izeni.rapidosqlite.table.Column.Companion.STRING

/**
 * Created by ner on 2/8/17.
 */
data class Toy(val id: Long, val name: String) : ChildDataTable {

    companion object {
        val TABLE_NAME = "Toy"

        val ID = Column(LONG, "Id")
        val NAME = Column(STRING, "Name")

        val COLUMNS = arrayOf(ID, NAME)

        val BUILDER = Builder()
    }

    override fun tableName() = TABLE_NAME

    override fun contentValues() = ContentValues().addAll(COLUMNS, id, name)

    class Builder : ItemBuilder<Toy> {
        override fun buildItem(cursor: Cursor, dataConnection: DataConnection): Toy {
            return Toy(cursor.getLong(cursor.getColumnIndex("$TABLE_NAME.${ID.name}")), cursor.getString(cursor.getColumnIndex("$TABLE_NAME.${NAME.name}")))
        }
    }
}