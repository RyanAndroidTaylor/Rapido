package com.dtp.simplemvp.mvp.presenter

import com.dtp.simplemvp.mvp.state.State

/**
 * Created by rtaylor on 9/29/16.
 */
interface StatePresenter<T: State> : Presenter {
    /**
     * This should be a lateinit object that the BasePresenter will setup before it calls any of the load methods
     */
    var stateKey: String
    var state: T

    fun load(presenterData: PresenterData?)
    fun saveState(presenterData: PresenterData)
    override fun destroy()

    fun newState(): T
}