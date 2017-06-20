package com.izeni.rapidocommon.transaction

import com.izeni.rapidocommon.e
import retrofit2.Response
import kotlin.collections.HashMap

/**
 * Created by ner on 11/21/16.
 */
object TransactionErrorParser {
    private val errors = HashMap<String, TransactionError>()
    private val throwableErrors = HashMap<Throwable, TransactionError>()

    fun registerError(errorMessage: String, error: TransactionError) {
        errors.put(errorMessage, error)
    }

    fun registerError(throwable: Throwable, error: TransactionError) {
        throwableErrors.put(throwable, error)
    }

    fun parseError(throwable: Throwable?): TransactionError {
        throwable?.let {
            if (throwableErrors.containsKey(throwable))
                return throwableErrors[throwable] ?: ThrowableError(throwable)
        }

        return ThrowableError(throwable)
    }

    fun parseError(errorCode: String?, errorMessage: String?, errorBody: String?): TransactionError {
        var error: TransactionError? = null

        if (errors.containsKey(errorBody))
            error = errors[errorBody]
        else if (errors.containsKey(errorMessage))
            error = errors[errorMessage]
        else if (errors.containsKey(errorCode))
            error = errors[errorCode]
        else if (!errorBody.isNullOrBlank())
            error = MessageError(errorBody!!)
        else if (!errorMessage.isNullOrBlank())
            error = MessageError(errorMessage!!)

        if (error == null) {
            e("Unable to find matching error for errorMessage: $errorMessage")

            return UnknownError()
        }

        return error
    }

    fun <T> parseRetrofitError(response: Response<T>): TransactionError {
        var error: TransactionError? = null

        if (errors.containsKey(response.errorBody()?.string()))
            error = errors[response.errorBody()?.string()]
        else if (errors.containsKey(response.message()))
            error = errors[response.message()]
        else if (errors.containsKey(response.code().toString()))
            error = errors[response.code().toString()]
        else if (!response.errorBody().string().isNullOrEmpty())
            error = MessageError(response.errorBody().string())
        else if (!response.message().isNullOrEmpty())
            error = MessageError(response.message())


        if (error == null) {
            e("Unable to find matching error for errorMessage: $response")

            error = UnknownError()
        }

        return error
    }
}