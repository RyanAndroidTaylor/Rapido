package com.izeni.rapidosqlite.query

import com.izeni.rapidosqlite.table.Column

/**
 * Created by ner on 2/16/17.
 */
class LeftJoin : Join() {

    private lateinit var leftTableName: String
    private lateinit var rightTableName: String

    private lateinit var leftTableColumn: Column
    private lateinit var rightTableColumn: Column

    fun setLeftTable(tableName: String, column: Column): LeftJoin {
        this.leftTableName = tableName
        this.leftTableColumn = column

        return this
    }

    fun setRightTable(tableName: String, column: Column): LeftJoin {
        this.rightTableName = tableName
        this.rightTableColumn = column

        return this
    }

    fun build(): String {
        return stringBuilder.append(leftTableName, leftJoin, rightTableName, on, leftTableName, period, leftTableColumn.name, equals, rightTableColumn.name).toString()
    }
}