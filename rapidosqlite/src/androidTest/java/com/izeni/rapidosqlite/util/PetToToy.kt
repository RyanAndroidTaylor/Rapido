package com.izeni.rapidosqlite.util

import android.content.ContentValues
import com.izeni.rapidosqlite.addAll
import com.izeni.rapidosqlite.table.Column
import com.izeni.rapidosqlite.table.DataTable

/**
 * Created by ner on 2/8/17.
 */
data class PetToToy(val petUuid: String, val toyUuid: String, override val androidId: Long = -1) : DataTable {

    companion object {
        val TABLE_NAME = "PetToToy"

        val PET_UUID = Column(String::class.java, "PetId")
        val TOY_UUID = Column(String::class.java, "ToyId")

        val COLUMNS = arrayOf(PET_UUID, TOY_UUID)
    }

    override fun tableName() = TABLE_NAME

    override fun contentValues() = ContentValues().addAll(COLUMNS, petUuid, toyUuid)
}