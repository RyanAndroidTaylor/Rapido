package com.izeni.rapidocommon.transaction

import com.izeni.rapidocommon.filterNetworkErrors
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

/**
 * Created by ner on 6/9/17.
 */
class TransactionObservable<T>(val observable: Observable<T>) {

    private val doOnSuccessItems = mutableListOf<(T) -> Unit>()
    private val doOnErrorItems = mutableListOf<(Throwable?) -> Unit>()

    fun watchSuccesses(block: (T) -> Unit): TransactionObservable<T> {
        doOnSuccessItems.add(block)

        return this
    }

    fun watchErrors(block: (Throwable?) -> Unit): TransactionObservable<T> {
        doOnErrorItems.add(block)

        return this
    }

    fun subscribe(subscribeOn: Scheduler? = null, observeOn: Scheduler? = null): Disposable {
        var observable = this.observable.filterNetworkErrors()

        observeOn?.let { observable = observable.observeOn(observeOn) }

        observable = observable.doOnNext {
            when (it) {
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

fun <T> Observable<T>.toTransactionObservable(): TransactionObservable<T> = TransactionObservable(this)