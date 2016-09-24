package com.dtp.simplemvp.database.query

/**
 * Created by ryantaylor on 9/22/16.
 */
data class Query(val tableName: String, val columns: Array<String>? = null, val selection: String? = null, val selectionArgs: Array<String>? = null, val order: String? = null, val limit: String? = null)