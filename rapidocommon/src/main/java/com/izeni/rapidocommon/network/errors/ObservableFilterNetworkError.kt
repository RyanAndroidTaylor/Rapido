package com.izeni.rapidocommon.network.errors

import io.reactivex.ObservableOperator
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

/**
 * Created by ner on 11/20/16.
 */
class ObservableFilterNetworkError<T: Transaction<*>>(val networkErrorHandler: NetworkErrorHandler) : ObservableOperator<T, T> {
    override fun apply(observer: Observer<in T>?): Observer<in T> {
        return Op(observer)
    }


    inner class Op(val child: Observer<in T>?) : Observer<T>, Disposable {

        var disposable: Disposable? = null

        override fun onNext(t: T) {
            when (t) {
                is Transaction.Failure<*> -> networkErrorHandler.handleError(t.error)
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
            child?.onError(t)
        }

        override fun isDisposed(): Boolean {
            return disposable?.isDisposed ?: true
        }

        override fun dispose() {
            disposable?.dispose()
        }
    }
}