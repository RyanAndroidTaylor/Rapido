package com.dtp.samplemvp.common.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.dtp.simplemvp.TableBuilder

/**
 * Created by ryantaylor on 9/23/16.
 */
class DatabaseOpenHelper(context: Context) : SQLiteOpenHelper(context, "com.dtp.samplemvp", null, 1) {

    override fun onCreate(db: SQLiteDatabase?) {
        val tableBuilder = TableBuilder()

        val itemCreate = tableBuilder.buildTable(Item.NAME, Item.COLUMNS)
        db?.execSQL(tableBuilder.buildTable(Item.NAME, Item.COLUMNS))
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }
}