package com.izeni.rapidocommon.transaction

import android.app.Activity
import android.os.Handler
import android.widget.Toast
import com.afollestad.materialdialogs.MaterialDialog
import com.izeni.rapidocommon.e

/**
 * Created by ner on 11/20/16.
 */
object TransactionErrorHandler {

    private var context: Activity? = null

    fun attach(context: Activity) {
        this.context = context
    }

    fun detach(context: Activity) {
        if (this.context == context)
            this.context = null
    }

    fun handleError(error: Throwable?) {
        context?.let {
            Handler(it.mainLooper).post {
                when (error) {
                    is LogThrowable -> e(error.message)
                    is ToastThrowable -> showToast(error.message ?: "Message not specified")
                    is DialogThrowable -> showDialog(error.title, error.message ?: "Message not specified")
                    else -> error?.printStackTrace()
                }
            }
        }
    }

    fun showToast(message: String) {
        context?.let { Toast.makeText(it, message, Toast.LENGTH_LONG).show() }
    }

    fun showDialog(title: String, message: String) {
        context?.let {
            MaterialDialog.Builder(it)
                    .title(title)
                    .content(message)
                    .positiveText("Ok")
                    .show()
        }
    }
}

class LogThrowable(message: String): Throwable(message)

class ToastThrowable(message: String): Throwable(message)

class DialogThrowable(val title: String, message: String): Throwable(message)

fun throwLogMessage(message: String): Nothing = throw LogThrowable(message)

fun throwToastMessage(message: String): Nothing = throw ToastThrowable(message)

fun throwDialogMessage(title: String, message: String): Nothing = throw DialogThrowable(title, message)