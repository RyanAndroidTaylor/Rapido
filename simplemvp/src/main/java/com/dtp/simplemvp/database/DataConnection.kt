package com.dtp.simplemvp.database

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.dtp.simplemvp.database.item_builder.ChildItemBuilder
import com.dtp.simplemvp.database.item_builder.ItemBuilder
import com.dtp.simplemvp.database.item_builder.ParentItemBuilder
import com.dtp.simplemvp.database.query.Query
import com.dtp.simplemvp.database.table.ChildDataTable
import com.dtp.simplemvp.database.table.Column
import com.dtp.simplemvp.database.table.DataTable
import com.dtp.simplemvp.database.table.ParentDataTable
import com.dtp.simplemvp.get
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

        if (item is ParentDataTable) {
            val children = item.getChildren()

            for (child in children)
                database.insertWithOnConflict(child.tableName(), null, child.contentValues(), conflictAlgorithm)
        }

        database.insertWithOnConflict(item.tableName(), null, item.contentValues(), conflictAlgorithm)
    }

    fun saveAll(items: List<DataTable>) {
        val database = this.database

        for (item in items) {
            database.insertWithOnConflict(item.tableName(), null, item.contentValues(), conflictAlgorithm)

            if (item is ParentDataTable) {
                val children = item.getChildren()

                for (child in children)
                    database.insertWithOnConflict(child.tableName(), null, child.contentValues(), conflictAlgorithm)
            }
        }
    }

    /**
     * If column is not null update based on the column and value. If column is null
     * update based on the id.
     */
    fun updateWithColumn(item: DataTable, column: Column, value: String) {
        database.update(item.tableName(), item.contentValues(), "${column.name} ?=", arrayOf(value))
    }

    fun updateWithId(item: DataTable, value: String) {
        database.update(item.tableName(), item.contentValues(), Column.ID.name, arrayOf())
    }

    /**
     * If column is not null delete based on the column and value. If column is null
     * delete based on the id.
     */
    fun delete(item: DataTable, column: Column, value: String) {
        database.delete(item.tableName(), column.name, arrayOf(value))
    }

    /**
     * Delete everything for the table name that matches the column and value
     */
    fun deleteAll(tableName: String) {
        database.delete(tableName, null, null)
    }

    fun <T> findFirst(builder: ItemBuilder<T>, query: Query): T? {
        val database = database

        var item: T? = null

        val cursor = database.query(query.tableName, query.columns, query.selection, query.selectionArgs, null, null, query.order, query.limit)

        if (cursor.moveToFirst()) {
            if (builder is ParentItemBuilder) {
                item = buildParentWithCursor(builder, cursor)
            } else {
                item = builder.buildItem(cursor)
            }
        }

        cursor.close()

        return item
    }

    fun <T> findAll(builder: ItemBuilder<T>, query: Query): List<T> {
        val items = ArrayList<T>()

        val cursor = database.query(query.tableName, query.columns, query.selection, query.selectionArgs, null, null, query.order, query.limit)

        if (builder is ParentItemBuilder) {
            while (cursor.moveToNext())
                buildParentWithCursor(builder, cursor)
        } else {
            while (cursor.moveToNext())
                items.add(builder.buildItem(cursor))
        }

        cursor.close()

        return items
    }

    private fun <T> buildParentWithCursor(builder: ParentItemBuilder<T>, cursor: Cursor): T {
        val children: List<ChildDataTable>?
        val parentUuid = cursor.get<String>(Column.UUID)

        children = ArrayList<ChildDataTable>()

        val childrenBuilderData = builder.getChildBuilders()

        for (buildData in childrenBuilderData) {
            children.addAll(findAllChildren(database, buildData.first, buildData.second, parentUuid))
        }

        return builder.buildItem(cursor, children)
    }

    private fun findAllChildren(database: SQLiteDatabase, tableName: String, builder: ChildItemBuilder, parentUuid: String): List<ChildDataTable> {
        val items = ArrayList<ChildDataTable>()

        val cursor = database.query(tableName, null, "${Column.UUID.name} =? ", arrayOf(parentUuid), null, null, null, null)

        while (cursor.moveToNext()) {
            items.add(builder.buildItem(cursor))
        }

        cursor.close()

        return items
    }
}