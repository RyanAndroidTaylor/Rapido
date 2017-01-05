package com.izeni.rapidocommon.network.errors

import com.izeni.rapidocommon.e
import java.util.*

/**
 * Created by ner on 11/21/16.
 */
object NetworkErrorParser {
    private val errors = HashMap<String, Error>()

    fun registerError(errorMessage: String, error: Error) {
        errors.put(errorMessage, error)
    }

    fun parseError(errorCode: String?, errorMessage: String?, errorBody: String?): Error {
        var error: Error? = null

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
}