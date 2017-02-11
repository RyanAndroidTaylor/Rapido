package com.izeni.rapidocommon.errors

import android.content.Context
import android.widget.Toast
import com.afollestad.materialdialogs.MaterialDialog

/**
 * Created by ner on 11/20/16.
 */
class TransactionErrorHandler(val context: Context, val onErrorHandled: () -> Unit, val customErrorHandler: CustomErrorHandler? = null) {

    fun handleError(error: Error) {
        when (error) {
            is ToastError -> {
                showToast(error.message)

                onErrorHandled()
            }
            is DialogError -> showDialog(error.title, error.message, onErrorHandled)
            is MessageError -> {
                showToast(error.message)

                onErrorHandled()
            }
            else -> customErrorHandler?.handleError(error, this)
        }
    }

    fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    fun showDialog(title: String, message: String, complete: (() -> Unit)?) {
        MaterialDialog.Builder(context)
                .title(title)
                .content(message)
                .positiveText("Ok")
                .onPositive { materialDialog, dialogAction -> complete?.invoke() }
                .show()
    }

    interface CustomErrorHandler {
        fun handleError(error: Error, transactionErrorHandler: TransactionErrorHandler)
    }
}