package com.izeni.rapidosqlite.util

import android.content.ContentValues
import android.database.Cursor
import com.izeni.rapidosqlite.util.Pet
import com.izeni.rapidosqlite.DataConnection
import com.izeni.rapidosqlite.addAll
import com.izeni.rapidosqlite.get
import com.izeni.rapidosqlite.item_builder.ItemBuilder
import com.izeni.rapidosqlite.query.QueryBuilder
import com.izeni.rapidosqlite.table.Column
import com.izeni.rapidosqlite.table.Column.Companion.INT
import com.izeni.rapidosqlite.table.Column.Companion.LONG
import com.izeni.rapidosqlite.table.Column.Companion.STRING
import com.izeni.rapidosqlite.table.DataTable
import com.izeni.rapidosqlite.table.ParentDataTable

/**
 * Created by ner on 2/8/17.
 */
data class Person(val id: Long, val name: String, val age: Int, val pets: List<Pet>) : ParentDataTable {
    companion object {
        val TABLE_NAME = "Person"

        val ID = Column(LONG, "Id", notNull = true, unique = true)
        val NAME = Column(STRING, "Name", notNull = true)
        val AGE = Column(INT, "Age", notNull = true)

        val COLUMNS = arrayOf(ID, NAME, AGE)

        val BUILDER = Builder()
    }

    override fun tableName() = TABLE_NAME

    override fun contentValues() = ContentValues().addAll(COLUMNS, id, name, age)

    override fun getChildren() = pets

    class Builder : ItemBuilder<Person> {
        override fun buildItem(cursor: Cursor, dataConnection: DataConnection): Person {
            val personId = cursor.get<Long>(ID)

            val petQuery = QueryBuilder
                    .with(Pet.TABLE_NAME)
                    .whereEquals(Pet.FOREIGN_KEY, personId)
                    .build()

            val pets = dataConnection.findAll(Pet.BUILDER, petQuery)

            return Person(personId, cursor.get(NAME), cursor.get(AGE), pets)
        }
    }
}