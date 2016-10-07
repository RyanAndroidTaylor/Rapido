package com.dtp.simplemvp.mvp.presenter

import com.dtp.simplemvp.mvp.view.ViewLayer

/**
 * Created by ryantaylor on 9/26/16.
 */
interface Presenter<V : ViewLayer> {
    var view: V?

    fun load()

    fun destroy() {
        view = null
    }
}