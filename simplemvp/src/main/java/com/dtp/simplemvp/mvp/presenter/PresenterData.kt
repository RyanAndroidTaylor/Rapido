package com.dtp.simplemvp.mvp.presenter

import android.os.Bundle
import com.dtp.simplemvp.mvp.state.State

/**
 * Created by ryantaylor on 9/26/16.
 */
class PresenterData(val loadedState: Bundle) {

    fun saveState(stateKey: String, state: State) {
        loadedState.putParcelable(stateKey, state)
    }

    fun <T: State> loadState(stateKey: String): T {
        return loadedState.getParcelable<T>(stateKey)
    }
}