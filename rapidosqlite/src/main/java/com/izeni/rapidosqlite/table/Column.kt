package com.izeni.rapidosqlite.table

import java.lang.reflect.Type

/**
 * Created by ryantaylor on 9/22/16.
 */
class Column(val type: Type, val name: String, val notNull: Boolean = false, val unique: Boolean = false, val foreignKey: Pair<String, Column>? = null, val defaultValue: Any? = null) {

    companion object {
        //TYPE
        val INT = Int::class.java
        val STRING = String::class.java
        val LONG = Long::class.java
        val BOOLEAN = Boolean::class.java

        // Constrains
        val NOT_NULL = "NOT NULL"
        val REFERENCES = "REFERENCES "
        val UNIQUE = "UNIQUE"

        val ANDROID_ID = Column(LONG, "_id")
    }
}