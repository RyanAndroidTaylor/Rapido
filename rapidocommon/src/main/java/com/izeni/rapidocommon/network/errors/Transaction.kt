package com.izeni.rapidocommon.network.errors

/**
 * Created by ner on 10/31/16.
 */
sealed class Transaction<T> {

    class Idle<T>: Transaction<T>()

    class Loading<T>(): Transaction<T>()

    class Success<T>(val value: T): Transaction<T>()

    class Failure<T>(val error: Error): Transaction<T>()
}