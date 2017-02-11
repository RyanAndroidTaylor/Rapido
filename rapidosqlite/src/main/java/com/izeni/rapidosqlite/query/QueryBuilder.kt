package com.izeni.rapidosqlite.query

import com.izeni.rapidosqlite.table.Column
import java.util.*

/**
 * Created by ryantaylor on 9/23/16.
 */
class QueryBuilder {
    private val EQUALS = " =? "
    private val LESS_THAN = " <? "
    private val LESS_THAN_OR_EQUAL = " <=?"
    private val GREATER_THAN = " >? "
    private val GREATER_THAN_OR_EQUAL = " >=? "
    private val OR = " OR "
    private val AND = " AND "
    private val ASCENDING = "ASC"
    private val DESENDING = "DESC"

    private var tableName: String? = null
    private var columns: Array<String>? = null
    private var whereColumns = ArrayList<String>()
    private var whereOperators = ArrayList<String>()
    private var whereCombinds = ArrayList<String>()
    private var selectionArgs = ArrayList<String>()
    private var order: String? = null
    private var limit: String? = null

    private var join: String? = null

    private fun addToSelectionArgs(value: Any) {
        when (value) {
            is String -> selectionArgs.add(value)
            is Int, is Long -> selectionArgs.add(value.toString())
            is Boolean -> selectionArgs.add(if (value) "1" else "0")
            else -> throw IllegalArgumentException("whereEquals only supports String, Int, Long and Boolean. value passed was $value")
        }
    }

    private fun where(column: Column, value: Any, expression: String): QueryBuilder {
        whereColumns.add(column.name)
        whereOperators.add(expression)

        addToSelectionArgs(value)

        return this
    }

    fun with(tableName: String): QueryBuilder {
        this.tableName = tableName

        return this
    }

    fun all(tableName: String): Query {
        this.tableName = tableName

        return this.build()
    }

    fun join(join: String): QueryBuilder {
        this.join = join

        return this
    }

    fun select(columns: Array<Column>): QueryBuilder {
        val columnStrings = Array(columns.size, { position -> columns[position].name })

        this.columns = columnStrings

        return this
    }

    fun whereEquals(tableName: String, column: Column, value: Any): QueryBuilder {
        whereColumns.add("$tableName.${column.name}")
        whereOperators.add(EQUALS)

        addToSelectionArgs(value)

        return this
    }

    fun whereEquals(column: Column, value: Any): QueryBuilder {
        return where(column, value, EQUALS)
    }

    fun whereLessThan(column: Column, value: Any): QueryBuilder {
        return where(column, value, LESS_THAN)
    }

    fun whereLessThanOrEqual(column: Column, value: Any): QueryBuilder {
        return where(column, value, LESS_THAN_OR_EQUAL)
    }

    fun whereGreaterThan(column: Column, value: Any): QueryBuilder {
        return where(column, value, GREATER_THAN)
    }

    fun whereGreaterThanOrEqual(column: Column, value: Any): QueryBuilder {
        return where(column, value, GREATER_THAN_OR_EQUAL)
    }

    fun or(column: Column): QueryBuilder {
        whereCombinds.add(OR)
        whereColumns.add(column.name)

        return this
    }

    fun and(column: Column): QueryBuilder {
        whereCombinds.add(AND)
        whereColumns.add(column.name)

        return this
    }

    fun ascending(column: Column): QueryBuilder {
        this.order = "${column.name} $ASCENDING"

        return this
    }

    fun descending(column: Column): QueryBuilder {
        this.order = "${column.name} $DESENDING"

        return this
    }

    fun limit(limit: Int): QueryBuilder {
        this.limit = limit.toString()

        return this
    }

    fun build(): Query {
        if (join != null) {
            return buildRawQuery()
        } else {
            return buildQuery()
        }
    }

    private fun buildQuery(): Query {
        insureValidQuery()

        var args: Array<String>? = null

        if (selectionArgs.size > 0) {
            args = Array(selectionArgs.size, { position -> selectionArgs[position] })
        }

        val selection = getSelectionString()

        return Query(tableName!!, columns, selection, args, order, limit)
    }

    private fun buildRawQuery(): RawQuery {
        join?.let { join ->
            var rawQuery: String

            if (columns != null) {
                rawQuery = "SELECT "

                columns!!.forEach {
                    rawQuery += "$tableName.$it, "
                }

                rawQuery = rawQuery.trim(' ')
                rawQuery = rawQuery.trim(',')

                rawQuery += " FROM $join "
            } else {
                rawQuery = "SELECT * FROM $join "
            }

            if (whereColumns.size > 0) {
                rawQuery += "WHERE "

                whereColumns.forEach { rawQuery += "$it=?," }
            }

            rawQuery = rawQuery.trim(',')

            var args: Array<String>? = null

            if (selectionArgs.size > 0) {
                args = Array(selectionArgs.size, { position -> selectionArgs[position] })
            }

            return RawQuery(rawQuery, args)
        }

        throw IllegalStateException("Join must not be null when building a raw query")
    }

    private fun getSelectionString(): String? {
        var selectionBuilder: StringBuilder? = null

        if (whereColumns.size > 0) {
            selectionBuilder = StringBuilder()

            for (index in 0..whereColumns.size - 1) {
                selectionBuilder.append(whereColumns[index])
                selectionBuilder.append(whereOperators[index])

                if (whereCombinds.size > index)
                    selectionBuilder.append(whereCombinds[index])
            }
        }

        return selectionBuilder?.toString()
    }

    private fun insureValidQuery() {
        if (whereColumns.size != whereOperators.size)
            throw IllegalStateException("There were not equal numbers of whereColumns and whereOperators")
    }
}