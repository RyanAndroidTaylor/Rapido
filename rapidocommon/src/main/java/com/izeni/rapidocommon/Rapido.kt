package com.izeni.rapidocommon

import android.content.Context
import com.izeni.rapidocommon.errors.TransactionErrorHandler
import com.izeni.rapidocommon.network.NetworkProvider

/**
 * Created by ner on 1/3/17.
 */
object Rapido {
    var rxBusLogging = false

    fun setDefaultTag(tag: String) {
        kLog.defaultTag = tag
    }

    fun setWrapping(wrap: Boolean) {
        kLog.enableLineWrapping = wrap
    }

    fun setLogLevel(@kLog.LoggingLevel level: Int) {
        kLog.loggingLevel = level
    }

    fun init(context: Context) {
        SharedPref.setPrefContext(context)
        NetworkProvider.setNetworkContext(context)
        TransactionErrorHandler.init(context)
    }
}