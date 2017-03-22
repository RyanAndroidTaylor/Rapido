package com.izeni.rapidosqlite.util

import android.content.ContentValues
import com.izeni.rapidosqlite.addAll
import com.izeni.rapidosqlite.table.Column
import com.izeni.rapidosqlite.table.DataTable

/**
 * Created by ner on 2/8/17.
 */
data class PetToToy(val petUuid: String, val toyUuid: String, val uuid: String = java.util.UUID.randomUUID().toString().replace("-", "")) : DataTable {

    companion object {
        val TABLE_NAME = "PetToToy"

        val PET_UUID = Column(String::class.java, "PetId")
        val TOY_UUID = Column(String::class.java, "ToyId")
        val UUID = Column(String::class.java, "Uuid")

        val COLUMNS = arrayOf(PET_UUID, TOY_UUID, UUID)
    }

    override fun tableName() = TABLE_NAME

    override fun id() = uuid

    override fun idColumn() = UUID

    override fun contentValues() = ContentValues().addAll(COLUMNS, petUuid, toyUuid, uuid)
}