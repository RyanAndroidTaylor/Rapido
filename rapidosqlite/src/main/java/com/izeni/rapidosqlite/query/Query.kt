package com.izeni.rapidosqlite.query

/**
 * Created by ryantaylor on 9/22/16.
 */
open class Query(val tableName: String, val columns: Array<String>? = null, val selection: String? = null, val selectionArgs: Array<String>? = null, val groupBy: String? = null, val order: String? = null, val limit: String? = null)