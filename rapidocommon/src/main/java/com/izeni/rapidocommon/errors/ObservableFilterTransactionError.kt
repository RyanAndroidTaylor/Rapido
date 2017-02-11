package com.izeni.rapidocommon.errors

import io.reactivex.ObservableOperator
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

/**
 * Created by ner on 11/20/16.
 */
class ObservableFilterTransactionError<T: Transaction<*, *>>(val transactionErrorHandler: TransactionErrorHandler) : ObservableOperator<T, T> {
    override fun apply(observer: Observer<in T>?): Observer<in T> {
        return Op(observer)
    }


    inner class Op(val child: Observer<in T>?) : Observer<T>, Disposable {

        var disposable: Disposable? = null

        override fun onNext(t: T) {
            when (t) {
                is Transaction.Failure<*, *> -> transactionErrorHandler.handleError(t.error)
                else -> child?.onNext(t)
            }
        }

        override fun onSubscribe(d: Disposable?) {
            disposable = d

            child?.onSubscribe(d)
        }

        override fun onComplete() {
            child?.onComplete()
        }

        override fun onError(t: Throwable?) {
            transactionErrorHandler.handleError(ThrowableError(t))
        }

        override fun isDisposed(): Boolean {
            return disposable?.isDisposed ?: true
        }

        override fun dispose() {
            disposable?.dispose()
        }
    }
}