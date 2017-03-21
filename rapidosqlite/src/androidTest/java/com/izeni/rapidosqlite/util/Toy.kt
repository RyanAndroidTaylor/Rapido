package com.izeni.rapidosqlite.util

import android.content.ContentValues
import android.database.Cursor
import com.izeni.rapidosqlite.DataConnection
import com.izeni.rapidosqlite.addAll
import com.izeni.rapidosqlite.get
import com.izeni.rapidosqlite.item_builder.ItemBuilder
import com.izeni.rapidosqlite.table.Column
import com.izeni.rapidosqlite.table.Column.Companion.ANDROID_ID
import com.izeni.rapidosqlite.table.DataTable

/**
 * Created by ner on 2/8/17.
 */
data class Toy(val uuid: String, val name: String, override var androidId: Long = -1) : DataTable {

    companion object {
        val TABLE_NAME = "Toy"

        val UUID = Column(String::class.java, "Uuid", notNull = true, unique = true)
        val NAME = Column(String::class.java, "Name")

        val COLUMNS = arrayOf(UUID, NAME, ANDROID_ID)

        val BUILDER = Builder()
    }

    override fun tableName() = TABLE_NAME

    override fun contentValues() = ContentValues().addAll(COLUMNS, uuid, name, androidId)

    class Builder : ItemBuilder<Toy> {
        override fun buildItem(cursor: Cursor, dataConnection: DataConnection): Toy {
            return Toy(cursor.get(UUID), cursor.get(NAME), cursor.get(ANDROID_ID))
        }
    }
}