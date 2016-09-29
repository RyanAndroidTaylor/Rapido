package com.dtp.simplemvp.mvp.presenter

import com.dtp.simplemvp.mvp.state.State
import com.dtp.simplemvp.mvp.state.StateManager
import com.dtp.simplemvp.mvp.view.ViewLayer

/**
 * Created by ryantaylor on 9/26/16.
 */
abstract class BaseStatePresenter<T: State, out V: ViewLayer>(val view: V) : StatePresenter<T> {

    override fun load(presenterData: PresenterData?) {
        if (presenterData != null) {
            state = presenterData.loadState(stateKey)

            loadFromSavedState()
        } else if (StateManager.hasState(stateKey)) {
            state = StateManager.getState(stateKey)

            loadFromSavedState()
        } else {
            state = newState()
            StateManager.addState(stateKey, state)

            load()
        }
    }

    /**
     * Called when loading from the state saved in StateManager.
     */
    protected abstract fun loadFromState()

    /**
     * Called when loading from savedInstanceState. The activity has been destroyed and recreated by the system
     */
    protected abstract fun loadFromSavedState()

    override fun saveState(presenterData: PresenterData) {
        presenterData.saveState(stateKey, state)
    }

    override fun destroy() {
        StateManager.removeState(stateKey)
    }
}