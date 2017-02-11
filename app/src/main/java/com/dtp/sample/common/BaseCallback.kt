package com.dtp.sample.common

/**
 * Created by ryantaylor on 9/26/16.
 */
interface BaseCallback<in T> {
    fun succeeded(item: T)
    fun failed(message: String?)
}