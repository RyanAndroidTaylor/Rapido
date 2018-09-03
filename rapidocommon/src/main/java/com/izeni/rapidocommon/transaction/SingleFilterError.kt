package com.izeni.rapidocommon.transaction

import io.reactivex.SingleObserver
import io.reactivex.SingleOperator
import io.reactivex.disposables.Disposable

/**
 * Created by ner on 7/10/17.
 */
class SingleFilterError<T> : SingleOperator<Transaction<T>, T> {
    override fun apply(observer: SingleObserver<in Transaction<T>>?): SingleObserver<in T> {
        return Op(observer)
    }


    inner class Op(val child: SingleObserver<in Transaction<T>>?) : SingleObserver<T>, Disposable {

        var disposable: Disposable? = null

        override fun onSuccess(value: T) {
            child?.onSuccess(Transaction.Success(value))
        }

        override fun onSubscribe(d: Disposable?) {
            disposable = d

            child?.onSubscribe(d)
        }

        override fun onError(t: Throwable?) {
            TransactionErrorHandler.handleError(t)

            child?.onSuccess(Transaction.Failure<T>(t))
        }

        override fun isDisposed(): Boolean {
            return disposable?.isDisposed ?: true
        }

        override fun dispose() {
            disposable?.dispose()
        }
    }
}