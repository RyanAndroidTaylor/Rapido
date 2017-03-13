package com.izeni.rapidocommon.errors

import com.izeni.rapidocommon.filterNetworkErrors
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.BehaviorSubject

/**
 * Created by ner on 2/28/17.
 */
class TransactionSubject<T, P> {
    private val subject: BehaviorSubject<Transaction<T, P>> = BehaviorSubject.create()

    fun onIdel() {
        subject.onNext(Transaction.Idle<T, P>())
    }

    fun onLoading(progress: P?) {
        subject.onNext(Transaction.Loading(progress))
    }

    fun onSuccess(item: T) {
        subject.onNext(Transaction.Success(item))
    }

    fun onError(error: Error) {
        subject.onNext(Transaction.Failure<T, P>(error))
    }

    fun subject(): Observable<Transaction<T, P>> {
        return subject.filterNetworkErrors()
    }

    fun mainThreadSubject(): Observable<Transaction<T, P>> {
        return subject.filterNetworkErrors()
                .observeOn(AndroidSchedulers.mainThread())
    }
}