package com.izeni.rapidocommon.network

import com.izeni.rapidocommon.network.errors.NetworkErrorParser
import com.izeni.rapidocommon.network.errors.Transaction
import io.reactivex.subjects.BehaviorSubject
import retrofit2.Response

/**
 * Created by ner on 1/5/17.
 */
class Service {
    fun <T> handleResponse(response: Response<T>, subject: BehaviorSubject<Transaction<T>>) {
        if (response.isSuccessful && response.body() != null) {
            subject.onNext(Transaction.Success(response.body()))
        } else {
            subject.onNext(Transaction.Failure(NetworkErrorParser.parseRetrofitError(response)))
        }
    }

    fun <T> handleResponseAndComplete(response: Response<T>, subject: BehaviorSubject<Transaction<T>>) {
        handleResponse(response, subject)

        subject.onComplete()
    }

    fun <T> startNewSubject(): BehaviorSubject<Transaction<T>> {
        val subject: BehaviorSubject<Transaction<T>> = BehaviorSubject.create()

        subject.onNext(Transaction.Loading())

        return subject
    }
}