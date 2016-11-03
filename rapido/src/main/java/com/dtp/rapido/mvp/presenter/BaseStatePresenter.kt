package com.dtp.rapido.mvp.presenter

import com.dtp.rapido.mvp.state.State
import com.dtp.rapido.mvp.view.ViewLayer

/**
 * Created by ryantaylor on 9/26/16.
 */
abstract class BaseStatePresenter<T: State, V: ViewLayer>() : StatePresenter<T, V> {

    override fun load(presenterData: PresenterData?) {
        if (presenterData != null) {
            loadFromState(presenterData.loadState(stateKey))
        }  else {
            load()
        }
    }

    /**
     * Called when a loaded from some saved state. Either savedInstanceState or from StateManager
     */
    protected abstract fun loadFromState(state: T)
}