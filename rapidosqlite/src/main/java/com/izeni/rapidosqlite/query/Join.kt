package com.izeni.rapidosqlite.query

/**
 * Created by ner on 2/16/17.
 */
abstract class Join {

    protected val stringBuilder = StringBuilder()

    protected val leftJoin = " LEFT JOIN "
    protected val on = " ON "
    protected val space = " "
    protected val period = "."
    protected val equals = " = "
}