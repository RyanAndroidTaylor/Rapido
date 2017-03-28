package com.izeni.rapidocommon.transaction

import io.reactivex.ObservableOperator
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

/**
 * Created by ner on 11/20/16.
 */
class ObservableFilterTransactionError<T, P> : ObservableOperator<Transaction<T, P>, Transaction<T, P>> {
    override fun apply(observer: Observer<in Transaction<T, P>>?): Observer<in Transaction<T, P>> {
        return Op(observer)
    }


    inner class Op(val child: Observer<in Transaction<T, P>>?) : Observer<Transaction<T, P>>, Disposable {

        var disposable: Disposable? = null

        override fun onNext(t: Transaction<T, P>) {
            if (t is Transaction.Failure<*, *>)
                TransactionErrorHandler.handleError(t.error)

            child?.onNext(t)
        }

        override fun onSubscribe(d: Disposable?) {
            disposable = d

            child?.onSubscribe(d)
        }

        override fun onComplete() {
            child?.onComplete()
        }

        override fun onError(t: Throwable?) {
            val error = TransactionErrorParser.parseError(t)

            TransactionErrorHandler.handleError(error)

            child?.onNext(Transaction.Failure<T, P>(error))
        }

        override fun isDisposed(): Boolean {
            return disposable?.isDisposed ?: true
        }

        override fun dispose() {
            disposable?.dispose()
        }
    }
}