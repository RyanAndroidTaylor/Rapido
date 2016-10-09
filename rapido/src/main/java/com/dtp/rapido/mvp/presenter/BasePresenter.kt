package com.dtp.rapido.mvp.presenter

import com.dtp.rapido.mvp.state.State
import com.dtp.rapido.mvp.state.StateManager
import com.dtp.rapido.mvp.view.ViewLayer

/**
 * Created by ryantaylor on 9/26/16.
 */
abstract class BaseStatePresenter<T: State, V: ViewLayer>() : StatePresenter<T, V> {

    override fun load(presenterData: PresenterData?) {
        if (presenterData != null) {
            state = presenterData.loadState(stateKey)

            reload()
        } else if (StateManager.hasState(stateKey)) {
            state = StateManager.getState(stateKey)

            reload()
        } else {
            state = newState()

            StateManager.addState(stateKey, state)

            load()
        }
    }

    /**
     * Called when a loaded from some saved state. Either savedInstanceState or from StateManager
     */
    protected abstract fun reload()

    override fun saveState(presenterData: PresenterData) {
        presenterData.saveState(stateKey, state)
    }

    override fun destroy() {
        super.destroy()

        StateManager.removeState(stateKey)
    }
}