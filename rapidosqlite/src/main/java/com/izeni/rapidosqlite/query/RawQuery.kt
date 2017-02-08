package com.izeni.rapidosqlite.query

/**
 * Created by ner on 2/7/17.
 */
class RawQuery(val query: String, selectionArgs: Array<String>?) : Query("N/A", null, null, selectionArgs, null, null)