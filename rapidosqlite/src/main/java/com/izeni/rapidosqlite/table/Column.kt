package com.izeni.rapidosqlite.table

/**
 * Created by ryantaylor on 9/22/16.
 */
class Column(val type: Any, val name: String, val notNull: Boolean = false, val unique: Boolean = false, val foreignKey: Pair<String, Column>? = null) {

    companion object {
        //TYPE
        val INT = 1
        val STRING = ""
        val LONG = 1L
        val BOOLEAN = false

        // Constrains
        val NOT_NULL = "NOT NULL"
        val REFERENCES = "REFERENCES "
        val UNIQUE = "UNIQUE"

        val ID = Column(LONG, "_id")
        val UUID = Column(STRING, "uuid")
    }
}