package com.izeni.rapidosqlite

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.izeni.rapidosqlite.item_builder.ItemBuilder
import com.izeni.rapidosqlite.query.Query
import com.izeni.rapidosqlite.table.Column
import com.izeni.rapidosqlite.table.DataTable
import com.izeni.rapidosqlite.table.ParentDataTable
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
        val database = database

        save(item, database)

        database.close()
    }

    fun saveAll(items: List<DataTable>) {
        val database = this.database

        items.forEach { save(it, database) }

        database.close()
    }

    private fun save(item: DataTable, database: SQLiteDatabase) {
        if (item is ParentDataTable) {
            val children = item.getChildren()

            for (child in children) {
                child.setParentForeignKey(item.parentForeignKey())

                database.insertWithOnConflict(child.tableName(), null, child.contentValues(), conflictAlgorithm)
            }
        }

        database.insertWithOnConflict(item.tableName(), null, item.contentValues(), conflictAlgorithm)
    }

    /**
     * If column is not null update based on the column and value. If column is null
     * update based on the id.
     */
    fun updateWithColumn(item: DataTable, column: Column, value: String) {
        val database = database

        database.update(item.tableName(), item.contentValues(), "${column.name} ?=", arrayOf(value))

        database.close()
    }

    fun updateWithId(item: DataTable) {
        val database = database

        database.update(item.tableName(), item.contentValues(), Column.ID.name, arrayOf())

        database.close()
    }

    /**
     * If column is not null delete based on the column and value. If column is null
     * delete based on the id.
     */
    fun delete(item: DataTable, column: Column, value: String) {
        val database = database

        database.delete(item.tableName(), column.name, arrayOf(value))

        database.close()
    }

    /**
     * Delete everything for the table name that matches the column and value
     */
    fun deleteAll(tableName: String) {
        val database = database

        database.delete(tableName, null, null)

        database.close()
    }

    fun <T> findFirst(builder: ItemBuilder<T>, query: Query): T? {
        val database = database

        var item: T? = null

        val cursor = getCursor(database, query)

        if (cursor.moveToFirst())
            item = builder.buildItem(cursor)

        cursor.close()

        database.close()

        return item
    }

    fun <T> findAll(builder: ItemBuilder<T>, query: Query): List<T> {
        val database = database

        val items = ArrayList<T>()

        val cursor = getCursor(database, query)

        while (cursor.moveToNext())
            items.add(builder.buildItem(cursor))

        cursor.close()

        database.close()

        return items
    }

    fun getCursor(database: SQLiteDatabase, query: Query): Cursor {
        return database.query(query.tableName, query.columns, query.selection, query.selectionArgs, null, null, query.order, query.limit)
    }
}