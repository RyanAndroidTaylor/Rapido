package com.dtp.rapido.mvp.presenter

import com.dtp.rapido.mvp.state.State
import com.dtp.rapido.mvp.view.ViewLayer

/**
 * Created by rtaylor on 9/29/16.
 */
interface StatePresenter<T: State, V: ViewLayer> : Presenter<V> {
    /**
     * This should be a lateinit object that the BasePresenter will setup before it calls any of the load methods
     */
    var stateKey: String
    var state: T

    fun load(presenterData: PresenterData?)
    fun saveState(presenterData: PresenterData)

    override fun destroy() {
        super.destroy()
    }

    fun newState(): T
}