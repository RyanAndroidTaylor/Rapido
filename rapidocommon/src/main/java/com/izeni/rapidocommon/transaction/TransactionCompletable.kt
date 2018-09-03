package com.izeni.rapidocommon.transaction

import io.reactivex.Completable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

/**
 * Created by ner on 9/6/17.
 */
class TransactionCompletable(private val completable: Completable) {

    private var watchComplete: (() -> Unit)? = null
    private var watchError: ((Throwable?) -> Unit)? = null

    fun watchComplete(block: () -> Unit): TransactionCompletable {
        watchComplete = block

        return this
    }

    fun watchError(block: (Throwable?) -> Unit): TransactionCompletable {
        watchError = block

        return this
    }

    fun subscribe(subscribeOn: Scheduler? = null, observeOn: Scheduler? = null): Disposable {
        var completable = this.completable

        subscribeOn?.let { completable = completable.subscribeOn(subscribeOn) }

        observeOn?.let { completable = completable.observeOn(observeOn) }

        return completable.subscribe({ watchComplete?.invoke() }, { watchError?.invoke(it) })
    }

    fun subscribeIoToMain(): Disposable = subscribe(Schedulers.io(), AndroidSchedulers.mainThread())

    fun subscribeCompToMain(): Disposable = subscribe(Schedulers.computation(), AndroidSchedulers.mainThread())
}

fun Completable.toTransactionCompletable(): TransactionCompletable = TransactionCompletable(this)