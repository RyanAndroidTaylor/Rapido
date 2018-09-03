package com.izeni.rapidocommon.transaction

import com.izeni.rapidocommon.filterNetworkErrors
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

/**
 * Created by ner on 7/10/17.
 */
class TransactionSingle<T>(val single: Single<T>) {

    private val doOnSuccessItems = mutableListOf<(T) -> Unit>()
    private val doOnErrorItems = mutableListOf<(Throwable?) -> Unit>()

    fun watchSuccesses(block: (T) -> Unit): TransactionSingle<T> {
        doOnSuccessItems.add(block)

        return this
    }

    fun watchErrors(block: (Throwable?) -> Unit): TransactionSingle<T> {
        doOnErrorItems.add(block)

        return this
    }

    fun subscribe(subscribeOn: Scheduler? = null, observeOn: Scheduler? = null): Disposable {
        var observable = this.single.filterNetworkErrors()

        observeOn?.let { observable = observable.observeOn(observeOn) }

        observable = observable.doOnSuccess {
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

fun <T> Single<T>.toTransactionSingle(): TransactionSingle<T> = TransactionSingle(this)