package com.izeni.rapidosqlite.table

import com.izeni.rapidosqlite.table.Column.Companion.NOT_NULL
import com.izeni.rapidosqlite.table.Column.Companion.REFERENCES
import com.izeni.rapidosqlite.table.Column.Companion.UNIQUE
import java.util.*

/**
 * Created by ryantaylor on 9/23/16.
 */
class TableBuilder {

    // Types
    val TEXT = "TEXT"
    val INTEGER = "INTEGER"
    val REAL = "REAL"
    val BOOLEAN = "INTEGER"
    val BLOB = "BLOB"

    private val SPACE = " "
    private val PERIOD = ""
    private val COMMA = ","
    private val DEFAULT = " DEFAULT "
    private val ANDROID_ID = "_id"

    private var createString = StringBuilder()

    private var foreignKeys = ArrayList<String>()

    private var currentTable: String? = null

    private var columns = ArrayList<String>()

    fun buildTable(tableName: String, columns: Array<Column>): String {
        open(tableName)

        for (column in columns) {
            val columnBuilder = getColumnBuilder(column)

            if (column.notNull)
                columnBuilder.notNull()
            if (column.unique)
                columnBuilder.unique()

            column.defaultValue?.let { columnBuilder.defaultValue(it) }

            column.foreignKey?.let { columnBuilder.foreignKey(it.tableName, it.column) }

            columnBuilder.build()
        }

        return retrieveCreateTableString()
    }

    private fun getColumnBuilder(column: Column): ColumnBuilder {
        when (column.type) {
            String::class.java -> return buildTextColumn(column.name)
            Int::class.java, Long::class.java -> return buildIntColumn(column.name)
            Float::class.java, Double::class.java -> return buildRealColumn(column.name)
            Boolean::class.java -> return buildBooleanColumn(column.name)
            else -> throw UnsupportedOperationException("No ColumnBuilder found for type ${column.type}")
        }
    }

    private fun open(tableName: String): String {
        prepareNewTable(tableName)

        createString.append("CREATE TABLE ")
        createString.append(currentTable)
        createString.append(" ( ")
        createString.append(ANDROID_ID)
        createString.append(" INTEGER PRIMARY KEY AUTOINCREMENT")

        columns.add("$currentTable$PERIOD$ANDROID_ID")

        return tableName
    }

    private fun prepareNewTable(tableName: String) {
        createString = StringBuilder()
        columns = ArrayList<String>()
        foreignKeys = ArrayList<String>()
        currentTable = tableName
    }

    private fun buildTextColumn(columnName: String): ColumnBuilder {
        return ColumnBuilder().appendText(columnName)
    }

    private fun buildIntColumn(columnName: String): ColumnBuilder {
        return ColumnBuilder().appendInt(columnName)
    }

    private fun buildRealColumn(columnName: String): ColumnBuilder {
        return ColumnBuilder().appendReal(columnName)
    }

    private fun buildBooleanColumn(columnName: String): ColumnBuilder {
        return ColumnBuilder().appendBoolean(columnName)
    }

    private fun buildBlobColumn(columnName: String): ColumnBuilder {
        return ColumnBuilder().appendBlob(columnName)
    }

    private fun retrieveCreateTableString(): String {
        createString.append(" ")

        for (foreignKey in foreignKeys) {
            createString.append(COMMA)
            createString.append(SPACE)
            createString.append(foreignKey)
        }

        createString.append(")")

        return createString.toString()
    }

    private inner class ColumnBuilder {
        private lateinit var columnName: String

        private var type: String? = null

        private val constraints: MutableList<String>

        private var defaultValue: Any? = null

        init {
            constraints = ArrayList<String>()
        }

        fun appendText(columnName: String): ColumnBuilder {
            this.columnName = columnName
            type = TEXT

            return this
        }

        fun appendInt(columnName: String): ColumnBuilder {
            this.columnName = columnName

            type = INTEGER

            return this
        }

        fun appendReal(columnName: String): ColumnBuilder {
            this.columnName = columnName

            type = REAL

            return this
        }

        fun appendBoolean(columnName: String): ColumnBuilder {
            this.columnName = columnName

            type = BOOLEAN

            return this
        }

        fun appendBlob(columnName: String): ColumnBuilder {
            this.columnName = columnName

            type = BLOB

            return this
        }

        fun notNull(): ColumnBuilder {
            constraints.add(NOT_NULL)

            return this
        }

        fun foreignKey(tableName: String, column: Column): ColumnBuilder {
            foreignKeys.add("FOREIGN KEY (${this.columnName}) $REFERENCES+$tableName(${column.name})")

            return this
        }

        fun unique(): ColumnBuilder {
            constraints.add(" $UNIQUE")

            return this
        }

        fun defaultValue(value: Any): ColumnBuilder {
            defaultValue = value

            return this
        }

        fun build(): String {
            createString.append(COMMA)
            createString.append(columnName)
            createString.append(SPACE)
            createString.append(type)

            for (constraint in constraints) {
                createString.append(SPACE)
                createString.append(constraint)
            }

            defaultValue?.let {
                createString.append(DEFAULT)

                when (it) {
                    is String, is Int, is Long, is Float, is Double -> createString.append(it)
                    is Boolean -> createString.append(if (it) 1 else 0)
                    else -> throw IllegalArgumentException("Default value must be of type String, Int, Long or Boolean. Default value type is ${it.javaClass}")
                }
            }

            columns.add(currentTable + PERIOD + columnName)

            return columnName
        }
    }
}