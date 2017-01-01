package com.dtp.rapidomvp.presenter

import com.dtp.rapidomvp.state.State
import com.dtp.rapidomvp.view.ViewLayer

/**
 * Created by ryantaylor on 9/26/16.
 */
abstract class BaseStatePresenter<T: State, V: ViewLayer> : StatePresenter<T, V> {

    override fun load(presenterData: PresenterData?) {
        if (presenterData != null) {
            loadFromState(presenterData.loadState(stateKey))
        }
    }

    /**
     * Called when loaded from some saved state. Either savedInstanceState or from StateManager
     */
    protected abstract fun loadFromState(state: T)
}