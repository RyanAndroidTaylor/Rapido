package com.izeni.rapidocommon.transaction

import com.izeni.rapidocommon.filterNetworkErrors
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.ReplaySubject

/**
 * Created by ner on 2/28/17.
 */
open class TransactionSubject<T, P> {
    protected val subject: ReplaySubject<Transaction<T, P>> = ReplaySubject.create()

    private val doOnIdleItems = mutableListOf<() -> Unit>()
    private val doOnProgressItems = mutableListOf<(P?) -> Unit>()
    private val doOnSuccessItems = mutableListOf<(T) -> Unit>()
    private val doOnErrorItems = mutableListOf<(Error) -> Unit>()
    private var runOnSubscribe: ((TransactionSubject<T, P>) -> Unit)? = null

    open fun onIdle() {
        if (!subject.hasComplete()) {
            subject.onNext(Transaction.Idle<T, P>())
        }
    }

    open fun onProgress(progress: P? = null) {
        if (!subject.hasComplete()) {
            subject.onNext(Transaction.Progress(progress))
        }
    }

    open fun onSuccess(item: T) {
        if (!subject.hasComplete()) {
            subject.onNext(Transaction.Success(item))
        }
    }

    open fun onError(error: Error) {
        if (!subject.hasComplete()) {
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

    fun runOnSubscribed(block: (TransactionSubject<T, P>) -> Unit): TransactionSubject<T, P> {
        runOnSubscribe = block

        return this
    }

    fun subscribe(subscribeOn: Scheduler? = null, observeOn: Scheduler? = null): Disposable {
        var observable = subject.filterNetworkErrors()

        observeOn?.let { observable = observable.observeOn(observeOn) }

        observable = observable.doOnSubscribe { runOnSubscribe?.invoke(this) }
                .doOnNext {
                    when (it) {
                        is Transaction.Idle -> doOnIdleItems.forEach { item -> item.invoke() }
                        is Transaction.Progress -> doOnProgressItems.forEach { item -> item.invoke(it.progress) }
                        is Transaction.Success -> doOnSuccessItems.forEach { item -> item.invoke(it.value) }
                        is Transaction.Failure -> doOnErrorItems.forEach { item -> item.invoke(it.error) }
                    }
                }

        subscribeOn?.let { return observable.subscribeOn(subscribeOn).subscribe() }

        return observable.subscribe()
    }

    fun subscribeIoToMain(): Disposable {
        return subscribe(Schedulers.io(), AndroidSchedulers.mainThread())
    }

    fun subscribeCompToMain(): Disposable {
        return subscribe(Schedulers.computation(), AndroidSchedulers.mainThread())
    }
}