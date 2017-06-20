package com.izeni.rapidocommon.transaction

/**
 * Created by ner on 10/31/16.
 */
sealed class Transaction<T> {

    companion object {
        fun <T> success(item: T): Transaction<T> = Success(item)
        fun <T> error(error: TransactionError): Transaction<T> = Failure(error)
    }

    class Success<T>(val value: T) : Transaction<T>()

    class Failure<T>(val error: TransactionError) : Transaction<T>()
}