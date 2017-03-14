package com.dtp.rapidomvp.presenter

import com.dtp.rapidomvp.state.State
import com.dtp.rapidomvp.view.ViewLayer

/**
 * Created by rtaylor on 9/29/16.
 */
abstract class StatePresenter<T: State, V: ViewLayer> : BasePresenter<V>() {

    abstract val stateKey: String

    abstract fun load(stateManager: stateManager)
    abstract fun saveState(stateManager: stateManager)
}