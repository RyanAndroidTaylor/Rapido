package com.izeni.rapidosqlite.table

import java.lang.reflect.Type

/**
 * Created by ryantaylor on 9/22/16.
 */
class Column(val type: Type, val name: String, val notNull: Boolean = false, val unique: Boolean = false, val foreignKey: ForeignKey? = null, val defaultValue: Any? = null) {

    companion object {

        // Constrains
        val NOT_NULL = "NOT NULL"
        val REFERENCES = "REFERENCES "
        val UNIQUE = "UNIQUE"
    }
}

class ForeignKey(val tableName: String, val column: Column)