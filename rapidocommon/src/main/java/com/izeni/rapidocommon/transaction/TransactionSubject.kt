package com.izeni.rapidocommon.transaction

import com.izeni.rapidocommon.filterNetworkErrors
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.BehaviorSubject

/**
 * Created by ner on 2/28/17.
 */
open class TransactionSubject<T, P> {
    protected val subject: BehaviorSubject<Transaction<T, P>> = BehaviorSubject.create()

    private var doOnIdle: (() -> Unit)? = null
    private var doOnLoading: ((P?) -> Unit)? = null
    private var doOnSuccess: ((T) -> Unit)? = null
    private var doOnError: ((Error) -> Unit)? = null

    open fun onIdle() {
        if (!subject.hasComplete()) {
            doOnIdle?.invoke()

            subject.onNext(Transaction.Idle<T, P>())
        }
    }

    open fun onLoading(progress: P? = null) {
        if (!subject.hasComplete()) {
            doOnLoading?.invoke(progress)

            subject.onNext(Transaction.Loading(progress))
        }
    }

    open fun onSuccess(item: T) {
        if (!subject.hasComplete()) {
            doOnSuccess?.invoke(item)

            subject.onNext(Transaction.Success(item))
        }
    }

    open fun onError(error: Error) {
        if (!subject.hasComplete()) {
            doOnError?.invoke(error)

            subject.onNext(Transaction.Failure<T, P>(error))
        }
    }

    fun doOnIdle(block: () -> Unit) {
        doOnIdle = block
    }

    fun doOnLoading(block: (P?) -> Unit) {
        doOnLoading = block
    }

    fun doOnError(block: (Error) -> Unit) {
        doOnError = block
    }

    fun doOnSuccess(block: (T) -> Unit) {
        doOnSuccess = block
    }

    fun subject(): Observable<Transaction<T, P>> {
        return subject.filterNetworkErrors()
    }

    fun mainThreadSubject(): Observable<Transaction<T, P>> {
        return subject.filterNetworkErrors()
                .observeOn(AndroidSchedulers.mainThread())
    }
}

class SingleTransactionSubject<T, P> : TransactionSubject<T, P>() {
    override fun onSuccess(item: T) {
        super.onSuccess(item)

        subject.onComplete()
    }
}

class CompleteOrErrorTransactionSubject<T, P> : TransactionSubject<T, P>() {
    override fun onSuccess(item: T) {
        super.onSuccess(item)

        subject.onComplete()
    }

    override fun onError(error: Error) {
        super.onError(error)

        subject.onComplete()
    }
}