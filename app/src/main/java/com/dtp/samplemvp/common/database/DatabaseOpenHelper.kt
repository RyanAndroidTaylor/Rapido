package com.dtp.samplemvp.common.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.izeni.rapidosqlite.table.TableBuilder

/**
 * Created by ryantaylor on 9/23/16.
 */
class DatabaseOpenHelper(context: Context) : SQLiteOpenHelper(context, "com.dtp.samplemvp", null, 1) {

    override fun onCreate(db: SQLiteDatabase?) {
        val tableBuilder = TableBuilder()

        val personTable = tableBuilder.buildTable(Person.TABLE_NAME, Person.COLUMNS)
        val petTable = tableBuilder.buildTable(Pet.TABLE_NAME, Pet.COLUMNS)
        db?.execSQL(personTable)
        db?.execSQL(petTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }
}