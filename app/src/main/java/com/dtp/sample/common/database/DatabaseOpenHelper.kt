package com.dtp.sample.common.database

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

        db?.execSQL(tableBuilder.buildTable(Person.TABLE_NAME, Person.COLUMNS))
        db?.execSQL(tableBuilder.buildTable(Pet.TABLE_NAME, Pet.COLUMNS))
        db?.execSQL(tableBuilder.buildTable(Toy.TABLE_NAME, Toy.COLUMNS))
        db?.execSQL(tableBuilder.buildTable(PetToToy.TABLE_NAME, PetToToy.COLUMNS))
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }
}