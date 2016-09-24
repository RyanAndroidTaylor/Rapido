package com.dtp.simplemvp

/**
 * Created by ryantaylor on 9/22/16.
 */
class Column(val name: String, val type: String, val notNull: Boolean = false, val unique: Boolean = false) {

    companion object {
        // Types
        val TEXT = "TEXT"
        val INTEGER = "INTEGER"
        val BOOLEAN = "INTEGER"
        val BLOB = "BLOB"

        // Constrains
        val NOT_NULL = "NOT NULL"
        val REFERENCES = "REFERENCES "
        val UNIQUE = "UNIQUE"

        val ID = Column("_id", INTEGER)
        val UUID = Column("uuid", TEXT)
    }
}