package com.dtp.samplemvp.common.database

import android.content.ContentValues
import android.database.Cursor
import com.izeni.rapidosqlite.addAll
import com.izeni.rapidosqlite.DataConnection
import com.izeni.rapidosqlite.item_builder.ItemBuilder
import com.izeni.rapidosqlite.query.QueryBuilder
import com.izeni.rapidosqlite.table.ChildDataTable
import com.izeni.rapidosqlite.table.Column
import com.izeni.rapidosqlite.table.Column.Companion.STRING
import com.izeni.rapidosqlite.table.ParentDataTable
import com.izeni.rapidosqlite.get

/**
 * Created by ner on 12/31/16.
 */
data class Person(val uuid: String, val name: String, val pets: List<Pet>) : ParentDataTable {

    companion object {
        val TABLE_NAME = "Person"

        val UUID = Column(STRING, "Uuid")
        val NAME = Column(STRING, "Name")

        val COLUMNS = arrayOf(UUID, NAME)

        val BUILDER = Builder()
    }

    override fun getChildren(): List<ChildDataTable> {
        return pets
    }

    override fun tableName() = TABLE_NAME

    override fun parentForeignKey() = uuid

    override fun contentValues(): ContentValues {
        return ContentValues().apply { addAll(COLUMNS, arrayOf(uuid, name)) }
    }

    class Builder : ItemBuilder<Person> {
        override fun buildItem(cursor: Cursor): Person {
            val personUuid = cursor.get<String>(UUID)

            val pets = com.izeni.rapidosqlite.DataConnection.findAll(Pet.BUILDER,
                                                                     QueryBuilder()
                                                      .from(Pet.TABLE_NAME)
                                                      .where(Pet.OWNER)
                                                      .equals(personUuid)
                                                      .build())

            return Person(personUuid, cursor.get(NAME), pets)
        }
    }
}