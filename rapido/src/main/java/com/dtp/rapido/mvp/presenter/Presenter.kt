package com.dtp.rapido.mvp.presenter

import com.dtp.rapido.mvp.view.ViewLayer

/**
 * Created by ryantaylor on 9/26/16.
 */
interface Presenter<V : ViewLayer> {
    var view: V?

    fun unSubscribe() {
        view = null
    }

    fun subscribe(view: V) {
        this.view = view
    }

    fun destroy() {
        view = null
    }
}