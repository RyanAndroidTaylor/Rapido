package com.dtp.simplemvp

import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.util.*

/**
 * Created by ryantaylor on 9/22/16.
 */
object DataConnection {

    var conflictAlgorithm = SQLiteDatabase.CONFLICT_REPLACE

    private lateinit var sqliteOpenHelper: SQLiteOpenHelper

    private val database: SQLiteDatabase
        get() = sqliteOpenHelper.writableDatabase

    fun init(databaseHelper: SQLiteOpenHelper) {
        this.sqliteOpenHelper = databaseHelper
    }

    fun save(item: DataTable) {
        database.insertWithOnConflict(item.tableName(), null, item.contentValues(), conflictAlgorithm)
    }

    fun bulkSave(items: List<DataTable>) {
        val database = this.database

        for (item in items) {
            database.insertWithOnConflict(item.tableName(), null, item.contentValues(), conflictAlgorithm)
        }
    }

    /**
     * If column is not null update based on the column and value. If column is null
     * update based on the id.
     */
    fun update(item: DataTable, column: Column? = null, value: String? = null) {

    }

    /**
     * Not sure if I need this
     */
    fun bulkUpdate(items: List<DataTable>, column: Column?, value: String?) {

    }

    /**
     * If column is not null delete based on the column and value. If column is null
     * delete based on the id.
     */
    fun delete(item: DataTable, column: Column? = null, value: String? = null) {

    }

    /**
     * Delete everything for the table name that matches the column and value
     */
    fun bulkDelete(tableName: String, column: Column, value: String) {

    }

    fun <T> queryFirst(builder: ItemBuilder<T>, query: Query): T? {
        var item: T? = null

        val cursor = database.query(query.tableName, query.columns, query.selection, query.selectionArgs, null, null, query.order, query.limit)

        if (cursor.moveToFirst()) {
            item = builder.buildItem(cursor)
        }

        cursor.close()

        return item
    }

    fun <T> queryAll(builder: ItemBuilder<T>, query: Query): List<T>? {
        var items: List<T>? = null

        val cursor = database.query(query.tableName, query.columns, query.selection, query.selectionArgs, null, null, query.order, query.limit)

        if (cursor.count > 0) {
            items = ArrayList<T>(cursor.count)

            while (cursor.moveToNext())
                items.add(builder.buildItem(cursor))
        }

        cursor.close()

        return items
    }
}