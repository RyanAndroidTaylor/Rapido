package com.izeni.rapidosqlite.util

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
            db?.execSQL(it.buildTable(Person.TABLE_NAME, Person.COLUMNS))
            db?.execSQL(it.buildTable(Pet.TABLE_NAME, Pet.COLUMNS))
            db?.execSQL(it.buildTable(Toy.TABLE_NAME, Toy.COLUMNS))
            db?.execSQL(it.buildTable(PetToToy.TABLE_NAME, PetToToy.COLUMNS))
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }
}