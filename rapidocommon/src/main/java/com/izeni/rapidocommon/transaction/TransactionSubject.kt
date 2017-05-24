package com.izeni.rapidocommon.transaction

import com.izeni.rapidocommon.filterNetworkErrors
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject

/**
 * Created by ner on 2/28/17.
 */
open class TransactionSubject<T, P> {
    protected val subject: BehaviorSubject<Transaction<T, P>> = BehaviorSubject.create()

    private val doOnIdleItems = mutableListOf<() -> Unit>()
    private val doOnProgressItems = mutableListOf<(P?) -> Unit>()
    private val doOnSuccessItems = mutableListOf<(T) -> Unit>()
    private val doOnErrorItems = mutableListOf<(Error) -> Unit>()
    private val doOnSubscribeItems = mutableListOf<(TransactionSubject<T, P>) -> Unit>()

    open fun onIdle() {
        if (!subject.hasComplete()) {
            doOnIdleItems.forEach { it.invoke() }

            subject.onNext(Transaction.Idle<T, P>())
        }
    }

    open fun onLoading(progress: P? = null) {
        if (!subject.hasComplete()) {
            doOnProgressItems.forEach { it.invoke(progress) }

            subject.onNext(Transaction.Loading(progress))
        }
    }

    open fun onSuccess(item: T) {
        if (!subject.hasComplete()) {
            doOnSuccessItems.forEach { it.invoke(item) }

            subject.onNext(Transaction.Success(item))
        }
    }

    open fun onError(error: Error) {
        if (!subject.hasComplete()) {
            doOnErrorItems.forEach { it.invoke(error) }

            subject.onNext(Transaction.Failure<T, P>(error))
        }
    }

    fun watchIdle(block: () -> Unit): TransactionSubject<T, P> {
        doOnIdleItems.add(block)

        return this
    }

    fun watchProgress(block: (P?) -> Unit): TransactionSubject<T, P> {
        doOnProgressItems.add(block)

        return this
    }

    fun watchSuccesses(block: (T) -> Unit): TransactionSubject<T, P> {
        doOnSuccessItems.add(block)

        return this
    }

    fun watchErrors(block: (Error) -> Unit): TransactionSubject<T, P> {
        doOnErrorItems.add(block)

        return this
    }

    fun subscribe(subscribeOn: Scheduler? = null, observeOn: Scheduler? = null): Disposable {
        var observable = subject.filterNetworkErrors()

        observeOn?.let { observable = observable.observeOn(observeOn) }

        observable = observable.doOnSubscribe { doOnSubscribeItems.forEach { it.invoke(this) } }

        subscribeOn?.let { observable = observable.subscribeOn(it) }

        return observable.subscribe()
    }

    fun subscribeIoToMain(): Disposable {
        return subscribe(Schedulers.io(), AndroidSchedulers.mainThread())
    }

    fun subscribeCompToMain(): Disposable {
        return subscribe(Schedulers.computation(), AndroidSchedulers.mainThread())
    }
}