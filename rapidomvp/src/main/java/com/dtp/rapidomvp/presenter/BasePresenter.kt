package com.dtp.rapidomvp.presenter

import com.dtp.rapidomvp.view.ViewLayer
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by ner on 1/31/17.
 */
abstract class BasePresenter<V : ViewLayer> : Presenter<V> {

    override var view: V? = null

    protected val subscriptions = CompositeDisposable()

    override fun unSubscribe() {
        super.unSubscribe()

        subscriptions.clear()
    }
}