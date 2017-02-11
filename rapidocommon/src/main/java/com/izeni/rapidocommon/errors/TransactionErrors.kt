package com.izeni.rapidocommon.errors

/**
 * Created by ner on 11/20/16.
 */

interface Error {
    val message: String
}

interface ToastError : Error

interface DialogError : Error {
    val title: String
}

class ThrowableError(val throwable: Throwable?) : Error {
    override val message = throwable?.message ?: "N/A"
}

class Unauthorized() : DialogError {
    override val title = "Unauthorized"
    override val message = "Username or password was incorrect."
}

class ExpiredLogin() : DialogError {
    override val title = "Expired login"
    override val message = "You login has expired please logout and log back in"
}

class EmailInUse() : DialogError {
    override val title = "Email in use"
    override val message = "An account has already been created with the email"
}

class InvalidEmail() : DialogError {
    override val title = "Invalid email"
    override val message = "The email you have entered is invalid. Please check you email and try again"
}

class UnknownError : ToastError {
    override val message = "Unknown error"
}

class NetworkError() : ToastError {
    override val message = "Failure due to poor internet connection"
}

class ServerError() : DialogError {
    override val title = "Server error"
    override val message = "Sorry for the inconvenience. Please try again"
}

class MessageError(override val message: String) : Error