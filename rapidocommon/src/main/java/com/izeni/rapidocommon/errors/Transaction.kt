package com.izeni.rapidocommon.errors

/**
 * Created by ner on 10/31/16.
 */
sealed class Transaction<T, P> {

    class Idle<T, P>: Transaction<T, P>()

    class Loading<T, P>(val progress: P? = null): Transaction<T, P>()

    class Success<T, P>(val value: T): Transaction<T, P>()

    class Failure<T, P>(val error: Error): Transaction<T, P>()
}