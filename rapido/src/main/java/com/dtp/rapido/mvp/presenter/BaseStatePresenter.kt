package com.dtp.rapido.mvp.presenter

import com.dtp.rapido.mvp.state.State
import com.dtp.rapido.mvp.state.StateManager
import com.dtp.rapido.mvp.view.ViewLayer

/**
 * Created by ryantaylor on 9/26/16.
 */
abstract class BaseStatePresenter<T: State, V: ViewLayer>() : StatePresenter<T, V> {

    override fun start(presenterData: PresenterData?) {
        if (presenterData != null) {
            state = presenterData.loadState(stateKey)

            startFromState()
        } else if (StateManager.hasState(stateKey)) {
            state = StateManager.getState(stateKey)

            startFromState()
        } else {
            state = newState()

            StateManager.addState(stateKey, state)

            start()
        }
    }

    /**
     * Called when a loaded from some saved state. Either savedInstanceState or from StateManager
     */
    protected abstract fun startFromState()

    override fun saveState(presenterData: PresenterData) {
        presenterData.saveState(stateKey, state)
    }

    override fun destroy() {
        super.destroy()

        StateManager.removeState(stateKey)
    }
}