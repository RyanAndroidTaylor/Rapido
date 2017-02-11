package com.dtp.sample.common.database

import android.content.ContentValues
import com.izeni.rapidosqlite.addAll
import com.izeni.rapidosqlite.table.Column
import com.izeni.rapidosqlite.table.JunctionDataTable

/**
 * Created by ner on 2/8/17.
 */
data class PetToToy(val petId: Long, val toyId: Long) : JunctionDataTable {

    companion object {
        val TABLE_NAME = "PetToToy"

        val PET_ID = Column(Column.LONG, "PetId")
        val TOY_ID = Column(Column.STRING, "ToyId")

        val COLUMNS = arrayOf(PET_ID, TOY_ID)
    }

    override fun tableName() = TABLE_NAME

    override fun contentValues() = ContentValues().addAll(COLUMNS, petId, toyId)
}