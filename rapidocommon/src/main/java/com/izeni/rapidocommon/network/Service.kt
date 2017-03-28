package com.izeni.rapidocommon.network

import com.izeni.rapidocommon.transaction.CompleteOrErrorTransactionSubject
import com.izeni.rapidocommon.transaction.TransactionErrorParser
import com.izeni.rapidocommon.transaction.TransactionSubject
import retrofit2.Response

/**
 * Created by ner on 1/5/17.
 */
abstract class Service {
    fun <T> handleResponse(response: Response<T>, subject: TransactionSubject<T, Unit>) {
        if (response.isSuccessful && response.body() != null) {
            subject.onSuccess(response.body())
        } else {
            subject.onError(TransactionErrorParser.parseRetrofitError(response))
        }
    }

    fun <T> handleResponseAndComplete(response: Response<T>, subject: CompleteOrErrorTransactionSubject<T, Unit>) {
        if (response.isSuccessful && response.body() != null) {
            subject.onSuccess(response.body())
        } else {
            subject.onError(TransactionErrorParser.parseRetrofitError(response))
        }
    }
}