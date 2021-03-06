package com.izeni.rapidocommon.transaction

import io.reactivex.ObservableOperator
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

/**
 * Created by ner on 11/20/16.
 */
class ObservableFilterTransactionError<T> : ObservableOperator<Transaction<T>, T> {
    override fun apply(observer: Observer<in Transaction<T>>?): Observer<in T> {
        return Op(observer)
    }


    inner class Op(val child: Observer<in Transaction<T>>?) : Observer<T>, Disposable {

        var disposable: Disposable? = null

        override fun onNext(t: T) {
            child?.onNext(Transaction.Success(t))
        }

        override fun onSubscribe(d: Disposable?) {
            disposable = d

            child?.onSubscribe(d)
        }

        override fun onComplete() {
            child?.onComplete()
        }

        override fun onError(t: Throwable?) {
            TransactionErrorHandler.handleError(t)

            child?.onNext(Transaction.Failure<T>(t))
        }

        override fun isDisposed(): Boolean {
            return disposable?.isDisposed ?: true
        }

        override fun dispose() {
            disposable?.dispose()
        }
    }
}