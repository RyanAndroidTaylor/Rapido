package com.izeni.rapidocommon.network

import com.izeni.rapidocommon.d
import com.izeni.rapidocommon.e
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException

interface NetworkCallback<in T> {
    fun onError(exception: HttpException)
    fun onSuccess(data: T)
    fun onFatal(exception: Throwable)

    companion object {
        fun <T> from(onSuccess: (T) -> Unit, onError: ((HttpException) -> Unit)? = null, onFatal: ((Throwable) -> Unit)? = null): NetworkCallback<T> {
            return object: NetworkCallback<T> {
                override fun onFatal(exception: Throwable) {
                    if(onFatal != null) onFatal(exception) else e("exception:", t = exception)
                }

                override fun onError(exception: HttpException) {
                    if(onError != null) onError(exception) else e("exception: ${exception.message()}", t = exception)
                }

                override fun onSuccess(data: T) {
                    onSuccess(data)
                }
            }
        }
    }
}

class SilentNetworkCallback(): NetworkCallback<Any?> {
    override fun onError(exception: HttpException) {

    }

    override fun onFatal(exception: Throwable) {

    }

    override fun onSuccess(data: Any?) {

    }
}

abstract class SimpleNetworkCallback<in T>(): NetworkCallback<T> {
    override fun onError(exception: HttpException) {
        e("exception: ${exception.message()}", t = exception)
    }

    override fun onFatal(exception: Throwable) {
        e("exception:", t = exception)
    }

    override fun onSuccess(data: T) {
        d("success: [$data]")
    }
}