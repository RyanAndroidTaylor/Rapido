package com.izeni.rapidosqlite

import android.annotation.SuppressLint
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.izeni.rapidosqlite.item_builder.ItemBuilder
import com.izeni.rapidosqlite.query.Query
import com.izeni.rapidosqlite.query.RawQuery
import com.izeni.rapidosqlite.table.Column
import com.izeni.rapidosqlite.table.DataTable
import com.izeni.rapidosqlite.table.ParentDataTable
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicInteger

/**
 * Created by ryantaylor on 9/22/16.
 */
class DataConnection private constructor(val database: SQLiteDatabase) {

    companion object {
        private val connectionCount = AtomicInteger()

        private val databaseExecutor = Executors.newSingleThreadExecutor()

        private val databaseSubject: PublishSubject<DataAction<*>> by lazy { PublishSubject.create<DataAction<*>>() }

        private lateinit var sqliteOpenHelper: SQLiteOpenHelper

        fun init(databaseHelper: SQLiteOpenHelper) {
            this.sqliteOpenHelper = databaseHelper
        }

        @Suppress("UNCHECKED_CAST")
        fun <T : DataTable> watchTableSaves(tableName: String): Flowable<SaveAction<T>> {
            return databaseSubject.filter { it is SaveAction<*> && it.tableName == tableName }
                    .map { it as SaveAction<T> }
                    .toFlowable(BackpressureStrategy.BUFFER)
        }

        @Suppress("UNCHECKED_CAST")
        fun <T : DataTable> watchTableUpdates(tableName: String): Flowable<UpdateAction<T>> {
            return databaseSubject.filter { it is UpdateAction<*> && it.tableName == tableName }
                    .map { it as UpdateAction<T> }
                    .toFlowable(BackpressureStrategy.BUFFER)
        }

        fun watchTableDelets(tableName: String): Flowable<DeleteAction> {
            return databaseSubject.filter { it is DeleteAction && it.tableName == tableName }
                    .map { it as DeleteAction }
                    .toFlowable(BackpressureStrategy.BUFFER)
        }

        fun <T> asyncGetAndClose(block: (DataConnection) -> T): Observable<T> {
            Log.i("DataConnection", "Called on thread: ${Thread.currentThread().name}")

            return Observable.just(Unit)
                    .subscribeOn(Schedulers.from(databaseExecutor))
                    .map {
                        Log.i("DataConnection", "Executed on thread: ${Thread.currentThread().name}")
                        val connection = openConnection()

                        val items = block(connection)

                        connection.close()

                        items
                    }
        }

        fun <T> getAndClose(block: (DataConnection) -> T): T {
            val connection = openConnection()

            val items = block(connection)

            connection.close()

            return items
        }

        fun asyncDoAndClose(block: (DataConnection) -> Unit): Observable<Unit> {
            Log.i("DataConnection", "Executed on thread: ${Thread.currentThread().name}")

            return Observable.just(Unit)
                    .subscribeOn(Schedulers.from(databaseExecutor))
                    .map {
                        Log.i("DataConnection", "Current Thread is: ${Thread.currentThread().name}")

                        val connection = openConnection()

                        block(connection)

                        connection.close()
                    }
        }

        fun doAndClose(block: (DataConnection) -> Unit) {
            val connection = openConnection()

            block(connection)

            connection.close()
        }

        private fun openConnection(): DataConnection {
            connectionCount.incrementAndGet()

            return DataConnection(sqliteOpenHelper.writableDatabase)
        }
    }

    var conflictAlgorithm = SQLiteDatabase.CONFLICT_REPLACE

    private fun close() {
        if (connectionCount.decrementAndGet() < 1)
            database.close()
    }

    fun save(item: DataTable) {
        database.transaction { save(item, it) }
    }

    fun saveAll(items: List<DataTable>) {
        database.transaction { database -> items.forEach { save(it, database) } }
    }

    private fun save(item: DataTable, database: SQLiteDatabase) {
        if (item is ParentDataTable) {
            item.getChildren()?.forEach { save(it, database) }
        }

        database.insertWithOnConflict(item.tableName(), null, item.contentValues(), conflictAlgorithm)

        databaseSubject.onNext(SaveAction(item.tableName(), item))
    }

    /**
     * If column is not null update based on the column and value. If column is null
     * update based on the id.
     */
    fun updateForColumn(item: DataTable, column: Column, value: Any) {
        val columnValue = when (value) {
            is String -> value
            is Int, is Long -> value.toString()
            is Boolean -> if (value) "1" else "0"
            else -> throw IllegalArgumentException("String, Int, Long and Boolean are the only supported types. You passed ${value.javaClass}")
        }

        database.transaction { it.update(item.tableName(), item.contentValues(), "${column.name}=?", arrayOf(columnValue)) }

        databaseSubject.onNext(UpdateAction(item.tableName(), item))
    }

    /**
     * If column is not null delete based on the column and value. If column is null
     * delete based on the id.
     */
    fun delete(tableName: String, column: Column, value: Any) {
        val columnValue = when (value) {
            is String -> value
            is Int, is Long -> value.toString()
            is Boolean -> if (value) "1" else "0"
            else -> throw IllegalArgumentException("String, Int, Long and Boolean are the only supported types. You passed ${value.javaClass}")
        }

        database.transaction { it.delete(tableName, "${column.name}=?", arrayOf(columnValue)) }

        databaseSubject.onNext(DeleteAction(tableName, column, value))
    }

    /**
     * Delete everything for the table name that matches the column and value
     */
    fun deleteAll(tableName: String) {
        database.transaction { it.delete(tableName, null, null) }
    }

    fun <T> findFirst(builder: ItemBuilder<T>, query: Query): T? {
        var item: T? = null

        database.transaction {
            val cursor = getCursor(database, query)

            if (cursor.moveToFirst())
                item = builder.buildItem(cursor, this)

            cursor.close()
        }

        return item
    }

    fun <T> findAll(builder: ItemBuilder<T>, query: Query): List<T> {
        val items = ArrayList<T>()

        database.transaction {
            val cursor = getCursor(database, query)

            while (cursor.moveToNext()) {
                items.add(builder.buildItem(cursor, this))
            }

            cursor.close()
        }

        return items
    }

    // Cursor should be closed in the block that calls this method
    @SuppressLint("Recycle")
    private fun getCursor(database: SQLiteDatabase, query: Query): Cursor {
        if (query is RawQuery) {
            return database.rawQuery(query.query, query.selectionArgs)
        } else {
            return database.query(query.tableName, query.columns, query.selection, query.selectionArgs, null, null, query.order, query.limit)
        }
    }

    abstract class DataAction<out T>(val tableName: String)

    class SaveAction<out T : DataTable>(tableName: String, val item: T) : DataAction<T>(tableName)

    class UpdateAction<out T : DataTable>(tableName: String, val item: T) : DataAction<T>(tableName)

    class DeleteAction(tableName: String, val column: Column, val value: Any) : DataAction<Unit>(tableName)
}