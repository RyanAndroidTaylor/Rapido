package com.dtp.sample.common.database

import android.content.ContentValues
import android.database.Cursor
import com.izeni.rapidosqlite.DataConnection
import com.izeni.rapidosqlite.addAll
import com.izeni.rapidosqlite.get
import com.izeni.rapidosqlite.item_builder.ItemBuilder
import com.izeni.rapidosqlite.query.QueryBuilder
import com.izeni.rapidosqlite.table.Column
import com.izeni.rapidosqlite.table.Column.Companion.ANDROID_ID
import com.izeni.rapidosqlite.table.Column.Companion.INT
import com.izeni.rapidosqlite.table.Column.Companion.STRING
import com.izeni.rapidosqlite.table.ParentDataTable

/**
 * Created by ner on 2/8/17.
 */
data class Person(val uuid: String, val name: String, val age: Int, val pets: List<Pet>, override var androidId: Long = -1) : ParentDataTable {
    companion object {
        val TABLE_NAME = "Person"

        val UUID = Column(STRING, "Uuid", notNull = true, unique = true)
        val NAME = Column(STRING, "Name", notNull = true)
        val AGE = Column(INT, "Age", notNull = true, defaultValue = 38)

        val COLUMNS = arrayOf(UUID, NAME, AGE, ANDROID_ID)

        val BUILDER = Builder()
    }

    override fun tableName() = TABLE_NAME

    override fun contentValues() = ContentValues().addAll(COLUMNS, uuid, name, age, androidId)

    override fun getChildren() = pets

    class Builder : ItemBuilder<Person> {
        override fun buildItem(cursor: Cursor, dataConnection: DataConnection): Person {
            val personUuid = cursor.get<String>(UUID)

            val petQuery = QueryBuilder
                    .with(Pet.TABLE_NAME)
                    .whereEquals(Pet.PERSON_UUID, personUuid)
                    .build()

            val pets = dataConnection.findAll(Pet.BUILDER, petQuery)

            return Person(personUuid, cursor.get(NAME), cursor.get(AGE), pets, cursor.get(ANDROID_ID))
        }
    }
}