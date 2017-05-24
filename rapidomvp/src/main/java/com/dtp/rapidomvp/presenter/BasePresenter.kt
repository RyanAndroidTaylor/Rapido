package com.dtp.rapidomvp.presenter

import com.dtp.rapidomvp.view.ViewLayer
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * Created by ner on 1/31/17.
 */
open class BasePresenter<V : ViewLayer> : Presenter<V> {

    override var view: V? = null

    private val shortSubscriptions = CompositeDisposable()
    private val longSubscriptions = CompositeDisposable()

    override fun unSubscribe() {
        super.unSubscribe()

        shortSubscriptions.clear()
    }

    override fun destroy() {
        super.destroy()

        longSubscriptions.clear()
    }

    protected fun addSubscription(disposable: Disposable, longSubscription: Boolean = true) {
        if (longSubscription)
            longSubscriptions.add(disposable)
        else
            shortSubscriptions.add(disposable)
    }
}