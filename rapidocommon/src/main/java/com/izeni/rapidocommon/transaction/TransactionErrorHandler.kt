package com.izeni.rapidocommon.transaction

import android.content.Context
import android.os.Handler
import android.widget.Toast
import com.afollestad.materialdialogs.MaterialDialog

/**
 * Created by ner on 11/20/16.
 */
object TransactionErrorHandler {

    private lateinit var context: Context

    fun init(context: Context) {
        this.context = context
    }

    fun handleError(error: Error) {
        Handler(context.mainLooper).post {
            when (error) {
                is ToastError -> showToast(error.message)
                is DialogError -> showDialog(error.title, error.message)
                is MessageError -> showToast(error.message)
                else -> throw IllegalArgumentException("Error type not supported $error")
            }
        }
    }

    fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    fun showDialog(title: String, message: String) {
        MaterialDialog.Builder(context)
                .title(title)
                .content(message)
                .positiveText("Ok")
                .show()
    }
}