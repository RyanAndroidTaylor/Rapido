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
import com.izeni.rapidosqlite.table.Column.Companion.LONG
import com.izeni.rapidosqlite.table.Column.Companion.STRING
import com.izeni.rapidosqlite.table.DataTable
import com.izeni.rapidosqlite.table.ParentDataTable

/**
 * Created by ner on 2/8/17.
 */
data class Pet(val id: Long, val foreignKey: Long, val name: String, val toys: List<Toy>) : ParentDataTable {

    companion object {
        val TABLE_NAME = "Pet"

        val ID = Column(LONG, "Id", notNull = true, unique = true)
        val FOREIGN_KEY = Column(LONG, "ForeignKey", notNull = true)
        val NAME = Column(STRING, "Name", notNull = true)

        val COLUMNS = arrayOf(ID, FOREIGN_KEY, NAME)

        val BUILDER = Builder()
    }

    override fun tableName() = TABLE_NAME

    override fun contentValues() = ContentValues().addAll(COLUMNS, id, foreignKey, name)

    override fun getChildren(): List<DataTable> {
        val children = mutableListOf<DataTable>()

        children.addAll(toys)
        children.addAll(toys.map { PetToToy(id, it.id) })

        return children
    }

    class Builder : ItemBuilder<Pet> {
        override fun buildItem(cursor: Cursor, dataConnection: DataConnection): Pet {
            val id = cursor.get<Long>(ID)

            val manyToMany = ManyToMany()
                    .returnTable(Toy.TABLE_NAME, Toy.ID)
                    .joinTable(TABLE_NAME, ID)
                    .junctionTable(PetToToy.TABLE_NAME, PetToToy.TOY_ID, PetToToy.PET_ID)
                    .build()


            val query = QueryBuilder
                    .with(Toy.TABLE_NAME)
                    .select(Toy.COLUMNS)
                    .whereEquals(TABLE_NAME, ID, id)
                    .join(manyToMany)
                    .build()
            val toys = dataConnection.findAll(Toy.BUILDER, query)

            return Pet(cursor.get(ID), cursor.get(FOREIGN_KEY), cursor.get(NAME), toys)
        }
    }
}