package com.izeni.rapidocommon.transaction

/**
 * Created by ner on 10/31/16.
 */
sealed class Transaction<T> {

    class Success<T>(val value: T) : Transaction<T>()

    class Failure<T>(val error: Throwable?) : Transaction<T>()
}