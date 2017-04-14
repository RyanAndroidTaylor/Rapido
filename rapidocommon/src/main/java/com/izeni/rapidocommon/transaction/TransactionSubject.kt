package com.izeni.rapidocommon.transaction

import com.izeni.rapidocommon.filterNetworkErrors
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject

/**
 * Created by ner on 2/28/17.
 */
open class TransactionSubject<T, P> {
    private val DEFAULT = 0
    private val IO = 1
    private val COMPUTATION = 2

    protected val subject: BehaviorSubject<Transaction<T, P>> = BehaviorSubject.create()

    private var doOnIdle: (() -> Unit)? = null
    private var doOnLoading: ((P?) -> Unit)? = null
    private var doOnSuccess: ((T) -> Unit)? = null
    private var doOnError: ((Error) -> Unit)? = null
    private var doOnSubscribe: (() -> Unit)? = null

    private var subscribeOn = DEFAULT

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

    fun doOnSubscribe(block: () -> Unit): TransactionSubject<T, P> {
        this.doOnSubscribe = block

        return this
    }

    fun subscribeOnIo() : TransactionSubject<T, P> {
        subscribeOn = IO

        return this
    }

    fun subscribeOnComputation() : TransactionSubject<T, P> {
        subscribeOn = COMPUTATION

        return this
    }

    fun subject(): Observable<Transaction<T, P>> {
        val observable = subject.filterNetworkErrors()
                .doOnSubscribe { doOnSubscribe?.invoke() }

        when (subscribeOn) {
            IO -> return observable.subscribeOn(Schedulers.io())
            COMPUTATION -> return observable.subscribeOn(Schedulers.computation())
            else -> return observable
        }
    }

    fun mainThreadSubject(): Observable<Transaction<T, P>> {
        val observable = subject.filterNetworkErrors()
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { doOnSubscribe?.invoke() }

        when (subscribeOn) {
            IO -> return observable.subscribeOn(Schedulers.io())
            COMPUTATION -> return observable.subscribeOn(Schedulers.computation())
            else -> return observable
        }
    }
}