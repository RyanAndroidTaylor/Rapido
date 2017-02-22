package com.izeni.rapidocommon.errors

import io.reactivex.subjects.BehaviorSubject

/**
 * Created by ner on 10/31/16.
 */
sealed class Transaction<T, P> {

    companion object {
        fun <T, P> createnewSubject(errorHandler: TransactionErrorHandler): BehaviorSubject<Transaction<T, P>> {
            return BehaviorSubject.create<Transaction<T, P>>().apply { lift(ObservableFilterTransactionError<Transaction<T, P>>(errorHandler)) }
        }

        fun <T, P> startNewSubject(errorHandler: TransactionErrorHandler): BehaviorSubject<Transaction<T, P>> {
            return createnewSubject<T, P>(errorHandler).apply { onNext(Loading()) }
        }
    }

    class Idle<T, P> : Transaction<T, P>()

    class Loading<T, P>(val progress: P? = null) : Transaction<T, P>()

    class Success<T, P>(val value: T) : Transaction<T, P>()

    class Failure<T, P>(val error: Error) : Transaction<T, P>()
}