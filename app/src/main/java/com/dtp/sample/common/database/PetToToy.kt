package com.dtp.sample.common.database

import android.content.ContentValues
import com.izeni.rapidosqlite.addAll
import com.izeni.rapidosqlite.table.Column
import com.izeni.rapidosqlite.table.Column.Companion.LONG
import com.izeni.rapidosqlite.table.DataTable

/**
 * Created by ner on 2/8/17.
 */
data class PetToToy(val petId: Long, val toyId: Long) : DataTable {

    companion object {
        val TABLE_NAME = "PetToToy"

        val PET_ID = Column(LONG, "PetId")
        val TOY_ID = Column(LONG, "ToyId")

        val COLUMNS = arrayOf(PET_ID, TOY_ID)
    }

    override fun tableName() = TABLE_NAME

    override fun contentValues() = ContentValues().addAll(COLUMNS, petId, toyId)
}