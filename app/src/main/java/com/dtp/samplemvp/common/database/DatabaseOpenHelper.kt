package com.dtp.samplemvp.common.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.dtp.simplemvp.database.table.TableBuilder

/**
 * Created by ryantaylor on 9/23/16.
 */
class DatabaseOpenHelper(context: Context) : SQLiteOpenHelper(context, "com.dtp.samplemvp", null, 1) {

    override fun onCreate(db: SQLiteDatabase?) {
        val tableBuilder = TableBuilder()

        db?.execSQL(tableBuilder.buildTable(Deal.TABLE_NAME, Deal.COLUMNS))
        db?.execSQL(tableBuilder.buildTable(Item.TABLE_NAME, Item.COLUMNS))
        db?.execSQL(tableBuilder.buildTable(Theme.TABLE_NAME, Theme.COLUMNS))
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }
}