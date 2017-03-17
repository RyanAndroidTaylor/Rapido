package com.dtp.sample.common.database

import android.content.ContentValues
import android.database.Cursor
import com.izeni.rapidosqlite.DataConnection
import com.izeni.rapidosqlite.addAll
import com.izeni.rapidosqlite.get
import com.izeni.rapidosqlite.item_builder.ItemBuilder
import com.izeni.rapidosqlite.query.ManyToMany
import com.izeni.rapidosqlite.query.QueryBuilder
import com.izeni.rapidosqlite.table.Column
import com.izeni.rapidosqlite.table.Column.Companion.ANDROID_ID
import com.izeni.rapidosqlite.table.Column.Companion.STRING
import com.izeni.rapidosqlite.table.DataTable
import com.izeni.rapidosqlite.table.ParentDataTable

/**
 * Created by ner on 2/8/17.
 */
data class Pet(val uuid: String, val personUuid: String, val name: String, val toys: List<Toy>, override val androidId: Long = -1) : ParentDataTable {

    companion object {
        val TABLE_NAME = "Pet"

        val UUID = Column(String::class.java, "Uuid", notNull = true, unique = true)
        val PERSON_UUID = Column(String::class.java, "PersonUuid", notNull = true)
        val NAME = Column(STRING, "Name", notNull = true)

        val COLUMNS = arrayOf(UUID, PERSON_UUID, NAME, ANDROID_ID)

        val BUILDER = Builder()
    }

    override fun tableName() = TABLE_NAME

    override fun contentValues() = ContentValues().addAll(COLUMNS, uuid, personUuid, name, androidId)

    override fun getChildren(): List<DataTable> {
        val children = mutableListOf<DataTable>()

        children.addAll(toys)
        children.addAll(toys.map { PetToToy(uuid, it.uuid) })

        return children
    }

    class Builder : ItemBuilder<Pet> {
        override fun buildItem(cursor: Cursor, dataConnection: DataConnection): Pet {
            val uuid = cursor.get<String>(UUID)

            val manyToMany = ManyToMany()
                    .returnTable(Toy.TABLE_NAME, UUID)
                    .joinTable(TABLE_NAME, UUID)
                    .junctionTable(PetToToy.TABLE_NAME, PetToToy.TOY_UUID, PetToToy.PET_UUID)
                    .build()


            val query = QueryBuilder
                    .with(Toy.TABLE_NAME)
                    .select(Toy.COLUMNS)
                    .whereEquals(TABLE_NAME, UUID, uuid)
                    .join(manyToMany)
                    .build()
            val toys = dataConnection.findAll(Toy.BUILDER, query)

            return Pet(uuid, cursor.get(PERSON_UUID), cursor.get(NAME), toys, cursor.get(ANDROID_ID))
        }
    }
}