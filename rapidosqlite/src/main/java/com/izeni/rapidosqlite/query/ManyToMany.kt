package com.izeni.rapidosqlite.query

import com.izeni.rapidosqlite.table.Column

/**
 * Created by ner on 2/8/17.
 */
class ManyToMany : Join() {

    private lateinit var returnTableName: String
    private lateinit var returnTableColumn: String

    private lateinit var joinTableName: String
    private lateinit var joinColumn: String

    private lateinit var junctionTableName: String
    private lateinit var junctionToReturnConnection: String
    private lateinit var junctionToJoinConnection: String

    fun returnTable(tableName: String, column: Column): ManyToMany {
        returnTableName = tableName
        returnTableColumn = column.name

        return this
    }

    fun joinTable(tableName: String, column: Column): ManyToMany {
        joinTableName = tableName
        joinColumn = column.name

        return this
    }

    fun junctionTable(tableName: String, returnConnection: Column, joinConnection: Column): ManyToMany {
        junctionTableName = tableName
        junctionToReturnConnection = returnConnection.name
        junctionToJoinConnection = joinConnection.name

        return this
    }

    fun build(): String {
        return stringBuilder.append(returnTableName,
                                    leftJoin, junctionTableName, on, returnTableName, period, returnTableColumn, equals, junctionToReturnConnection,
                                    leftJoin, joinTableName, on, joinTableName, period, joinColumn, equals, junctionToJoinConnection)
                .toString()
    }
}