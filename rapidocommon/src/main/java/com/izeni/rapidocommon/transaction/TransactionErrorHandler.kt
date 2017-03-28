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

    fun dettach(context: Activity) {
        if (this.context == context)
            this.context = null
    }

    fun handleError(error: Error) {
        context?.let {
            Handler(it.mainLooper).post {
                when (error) {
                    is ToastError -> showToast(error.message)
                    is DialogError -> showDialog(error.title, error.message)
                    is MessageError -> showToast(error.message)
                    is LogError -> e(error.message)
                    else -> throw IllegalArgumentException("Error type not supported $error")
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