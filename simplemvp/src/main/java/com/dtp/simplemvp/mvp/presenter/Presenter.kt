package com.dtp.simplemvp.mvp.presenter

import com.dtp.simplemvp.mvp.state.State

/**
 * Created by ryantaylor on 9/26/16.
 */
interface Presenter<T: State> {
    var stateKey: String
    var state: T

    fun load(presenterData: PresenterData? = null)
    fun saveState(presenterData: PresenterData)
    fun destroy()

    fun newState(): T
}