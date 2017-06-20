package com.izeni.rapidocommon.transaction

import java.lang.Exception

/**
 * Created by ner on 11/20/16.
 */

abstract class TransactionError : Exception() {
    override val message: String? = null
}

abstract class ToastError : TransactionError()

abstract class DialogError : TransactionError() {
    open val title: String? = null
}

open class LogError(override val message: String) : TransactionError()

class ThrowableError(val throwable: Throwable?) : TransactionError() {
    override val message = throwable?.message ?: "N/A"
}

class Unauthorized() : DialogError() {
    override val title = "Unauthorized"
    override val message = "Username or password was incorrect."
}

class ExpiredLogin() : DialogError() {
    override val title = "Expired login"
    override val message = "You login has expired please logout and log back in"
}

class EmailInUse() : DialogError() {
    override val title = "Email in use"
    override val message = "An account has already been created with the email"
}

class InvalidEmail() : DialogError() {
    override val title = "Invalid email"
    override val message = "The email you have entered is invalid. Please check you email and try again"
}

class UnknownError : ToastError() {
    override val message = "Unknown error"
}

class NetworkError() : ToastError() {
    override val message = "Failure due to poor internet connection"
}

class ServerError() : DialogError() {
    override val title = "Server error"
    override val message = "Sorry for the inconvenience. Please try again"
}

class MessageError(override val message: String) : TransactionError()