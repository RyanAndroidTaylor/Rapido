package com.izeni.rapidocommon.network

import com.izeni.rapidocommon.transaction.TransactionErrorParser
import com.izeni.rapidocommon.transaction.Transaction
import io.reactivex.subjects.BehaviorSubject
import retrofit2.Response

/**
 * Created by ner on 1/5/17.
 */
class Service {
    fun <T> handleResponse(response: Response<T>, subject: BehaviorSubject<Transaction<T, Unit>>) {
        if (response.isSuccessful && response.body() != null) {
            subject.onNext(Transaction.Success(response.body()))
        } else {
            subject.onNext(Transaction.Failure(TransactionErrorParser.parseRetrofitError(response)))
        }
    }

    fun <T> handleResponseAndComplete(response: Response<T>, subject: BehaviorSubject<Transaction<T, Unit>>) {
        handleResponse(response, subject)

        subject.onComplete()
    }

    fun <T> startNewSubject(): BehaviorSubject<Transaction<T, Unit>> {
        val subject: BehaviorSubject<Transaction<T, Unit>> = BehaviorSubject.create()

        subject.onNext(Transaction.Loading())

        return subject
    }
}