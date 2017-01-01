package com.dtp.samplemvp.common.database

import android.content.ContentValues
import android.database.Cursor
import com.izeni.rapidosqlite.addAll
import com.izeni.rapidosqlite.item_builder.ItemBuilder
import com.izeni.rapidosqlite.table.ChildDataTable
import com.izeni.rapidosqlite.table.Column
import com.izeni.rapidosqlite.table.Column.Companion.INT
import com.izeni.rapidosqlite.table.Column.Companion.STRING
import com.izeni.rapidosqlite.get

/**
 * Created by ner on 12/31/16.
 */
data class Pet(val uuid: String, val type: Int, var ownerUuid: String? = null) : ChildDataTable {

    companion object {
        val TABLE_NAME = "Pet"

        val UUID = Column(STRING, "Uuid")
        val OWNER = Column(STRING, "Owner")
        val TYPE = Column(INT, "Type")

        val COLUMNS = arrayOf(UUID, OWNER, TYPE)

        val BUILDER = Builder()
    }

    override fun setParentForeignKey(foreignKey: String) {
        ownerUuid = foreignKey
    }

    override fun tableName() = TABLE_NAME

    override fun contentValues(): ContentValues {
        return ContentValues().apply { addAll(COLUMNS, arrayOf(uuid, ownerUuid, type)) }
    }

    class Builder : ItemBuilder<Pet> {
        override fun buildItem(cursor: Cursor): Pet {
            return Pet(cursor.get(UUID), cursor.get(TYPE), cursor.get(OWNER))
        }
    }
}