package com.dtp.rapidomvp.presenter

import com.dtp.rapidomvp.state.State
import com.dtp.rapidomvp.view.ViewLayer

/**
 * Created by rtaylor on 9/29/16.
 */
interface StatePresenter<T: State, V: ViewLayer> : Presenter<V> {
    /**
     * This should be a lateinit object that the BasePresenter will setup before it calls any of the load methods
     */
    var stateKey: String

    fun load(presenterData: PresenterData?)
    fun saveState(presenterData: PresenterData)

    override fun destroy() {
        super.destroy()
    }
}