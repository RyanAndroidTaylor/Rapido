package com.dtp.simplemvp.mvp.presenter

import com.dtp.simplemvp.mvp.state.State
import com.dtp.simplemvp.mvp.state.StateManager

/**
 * Created by ryantaylor on 9/26/16.
 */
abstract class BasePresenter<T: State> : Presenter<T> {

    override fun load(presenterData: PresenterData?) {
        if (presenterData != null) {
            state = presenterData.loadState(stateKey)
        } else if (StateManager.hasState(stateKey)) {
            state = StateManager.getState(stateKey)
        } else {
            state = newState()
            StateManager.addState(stateKey, state)
        }

        onStateLoaded()
    }

    /**
     * Called once the state has been loaded. The state can be a new state or loaded from the presenterData or StateManager
     * This is where you should handle the loading logic of your presenter.
     */
    abstract fun onStateLoaded()

    override fun saveState(presenterData: PresenterData) {
        presenterData.saveState(stateKey, state)
    }

    override fun destroy() {
        StateManager.removeState(stateKey)
    }
}