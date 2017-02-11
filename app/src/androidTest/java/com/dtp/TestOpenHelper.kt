package com.dtp

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.izeni.rapidosqlite.table.TableBuilder

/**
 * Created by ner on 2/10/17.
 */
class TestOpenHelper(context: Context) : SQLiteOpenHelper(context, "testingDatabase", null, 1) {

    override fun onCreate(db: SQLiteDatabase?) {
        TableBuilder().let {
            db?.execSQL(it.buildTable(com.dtp.sample.common.database.Person.TABLE_NAME, com.dtp.sample.common.database.Person.COLUMNS))
            db?.execSQL(it.buildTable(com.dtp.sample.common.database.Pet.TABLE_NAME, com.dtp.sample.common.database.Pet.COLUMNS))
            db?.execSQL(it.buildTable(com.dtp.sample.common.database.Toy.TABLE_NAME, com.dtp.sample.common.database.Toy.COLUMNS))
            db?.execSQL(it.buildTable(com.dtp.sample.common.database.PetToToy.TABLE_NAME, com.dtp.sample.common.database.PetToToy.COLUMNS))
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }
}