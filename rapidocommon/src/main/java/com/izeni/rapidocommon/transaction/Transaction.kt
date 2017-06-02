package com.izeni.rapidocommon.transaction

/**
 * Created by ner on 10/31/16.
 */
sealed class Transaction<T, P> {

    class Idle<T, P> : Transaction<T, P>()

    class Progress<T, P>(val progress: P? = null) : Transaction<T, P>()

    class Success<T, P>(val value: T) : Transaction<T, P>()

    class Failure<T, P>(val error: Error) : Transaction<T, P>()
}