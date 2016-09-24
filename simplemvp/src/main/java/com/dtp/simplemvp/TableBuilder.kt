package com.dtp.simplemvp

import java.util.*

import com.dtp.simplemvp.Column.Companion.TEXT
import com.dtp.simplemvp.Column.Companion.INTEGER
import com.dtp.simplemvp.Column.Companion.BOOLEAN
import com.dtp.simplemvp.Column.Companion.BLOB
import com.dtp.simplemvp.Column.Companion.NOT_NULL
import com.dtp.simplemvp.Column.Companion.REFERENCES
import com.dtp.simplemvp.Column.Companion.UNIQUE
import com.dtp.simplemvp.Column.Companion.ID
import com.dtp.simplemvp.Column.Companion.UUID

/**
 * Created by ryantaylor on 9/23/16.
 */
class TableBuilder {

    private val SPACE = " "
    private val PERIOD = "."
    private val COMMA = ","

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

            columnBuilder.build()
        }

        return retrieveCreateTableString()
    }

    private fun getColumnBuilder(column: Column): ColumnBuilder {
        when (column.type) {
            TEXT -> return buildTextColumn(column.name)
            INTEGER -> return buildIntColumn(column.name)
            BOOLEAN -> return buildBooleanColumn(column.name)
            BLOB -> return buildBlobColumn(column.name)
            else -> throw UnsupportedOperationException("No ColumnBuilder found for type ${column.type}")
        }
    }

    private fun open(tableName: String): String {
        prepareNewTable(tableName)

        createString.append("CREATE TABLE ")
        createString.append(currentTable)
        createString.append(" ( ")
        createString.append(ID.name)
        createString.append(" INTEGER PRIMARY KEY AUTOINCREMENT,")
        createString.append(UUID.name)
        createString.append(SPACE)
        createString.append(TEXT)
        createString.append(SPACE)
        createString.append(NOT_NULL)
        createString.append(SPACE)
        createString.append(UNIQUE)

        columns.add(currentTable + PERIOD + ID.name)
        columns.add(currentTable + PERIOD + UUID.name)

        return tableName
    }

    private fun openWithUuidForeignKeyRestraint(tableName: String, referenceTable: String): String {
        prepareNewTable(tableName)

        createString.append("CREATE TABLE ")
        createString.append(currentTable)
        createString.append(" ( ")
        createString.append(ID.name)
        createString.append(" INTEGER PRIMARY KEY AUTOINCREMENT,")
        createString.append(UUID.name)
        createString.append(SPACE)
        createString.append(TEXT)
        createString.append(SPACE)
        createString.append(NOT_NULL)
        createString.append(SPACE)
        createString.append(UNIQUE)
        createString.append(" REFERENCES ")
        createString.append(referenceTable)
        createString.append("(")
        createString.append(UUID.name)
        createString.append(")")

        columns.add(currentTable + PERIOD + ID.name)
        columns.add(currentTable + PERIOD + UUID.name)

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

        fun foreignKey(tableName: String, columnName: String): ColumnBuilder {
            foreignKeys.add("FOREIGN KEY (" + this.columnName + ") " + REFERENCES + tableName + "(" + columnName + ")")

            return this
        }

        fun unique(): ColumnBuilder {
            constraints.add(" $UNIQUE")

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

            columns.add(currentTable + PERIOD + columnName)

            return columnName
        }
    }
}