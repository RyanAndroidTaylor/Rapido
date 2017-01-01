package com.dtp.samplemvp.common.database

import android.content.ContentValues
import android.database.Cursor
import com.dtp.rapido.addAll
import com.dtp.rapido.database.DataConnection
import com.dtp.rapido.database.item_builder.ItemBuilder
import com.dtp.rapido.database.query.QueryBuilder
import com.dtp.rapido.database.table.ChildDataTable
import com.dtp.rapido.database.table.Column
import com.dtp.rapido.database.table.Column.Companion.STRING
import com.dtp.rapido.database.table.ParentDataTable
import com.dtp.rapido.get

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

            val pets = DataConnection.findAll(Pet.BUILDER,
                                              QueryBuilder()
                                                      .from(Pet.TABLE_NAME)
                                                      .where(Pet.OWNER)
                                                      .equals(personUuid)
                                                      .build())

            return Person(personUuid, cursor.get(NAME), pets)
        }
    }
}