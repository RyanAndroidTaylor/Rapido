package com.dtp.rapidomvp.presenter

import com.dtp.rapidomvp.view.ViewLayer
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * Created by ner on 1/31/17.
 */
open class BasePresenter<V : ViewLayer> : Presenter<V> {

    override var view: V? = null

    private val subscriptions = CompositeDisposable()
    private val fullSubscriptions = CompositeDisposable()

    override fun unSubscribe() {
        super.unSubscribe()

        subscriptions.clear()
    }

    override fun destroy() {
        super.destroy()

        fullSubscriptions.clear()
    }

    fun addSubscription(disposable: Disposable, fullSubscription: Boolean = true) {
        if (fullSubscription)
            fullSubscriptions.add(disposable)
        else
            subscriptions.add(disposable)
    }
}