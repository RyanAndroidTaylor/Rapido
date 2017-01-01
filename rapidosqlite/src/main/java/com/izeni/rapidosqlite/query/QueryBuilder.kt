package com.izeni.rapidosqlite.query

import com.izeni.rapidosqlite.query.Query
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

    fun select(columns: Array<Column>): QueryBuilder {
        val columnStrings = Array(columns.size, { position -> columns[position].name })

        this.columns = columnStrings

        return this
    }

    fun from(tableName: String): QueryBuilder {
        this.tableName = tableName

        return this
    }

    fun where(column: Column): QueryBuilder {
        whereColumns.add(column.name)

        return this
    }

    fun equals(value: String): QueryBuilder {
        whereOperators.add(EQUALS)
        selectionArgs.add(value)

        return this
    }

    fun lessThan(value: String): QueryBuilder {
        whereOperators.add(LESS_THAN)
        selectionArgs.add(value)

        return this
    }

    fun lessThanOrEqual(value: String): QueryBuilder {
        whereOperators.add(LESS_THAN_OR_EQUAL)
        selectionArgs.add(value)

        return this
    }

    fun greaterThan(value: String): QueryBuilder {
        whereOperators.add(GREATER_THAN)
        selectionArgs.add(value)

        return this
    }

    fun greaterThanOrEqual(value: String): QueryBuilder {
        whereOperators.add(GREATER_THAN_OR_EQUAL)
        selectionArgs.add(value)

        return this
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
        insureValidQuery()

        var args: Array<String>? = null

        if (selectionArgs.size > 0) {
            args = Array(selectionArgs.size, { position -> selectionArgs[position] })
        }

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

        val selection = selectionBuilder?.toString() ?: null

        return Query(tableName!!, columns, selection, args, order, limit)
    }

    private fun insureValidQuery() {
        if (whereColumns.size != whereOperators.size)
            throw IllegalStateException("There were not equal numbers of whereColumns and whereOperators")
    }
}