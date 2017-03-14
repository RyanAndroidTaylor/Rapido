package com.dtp.rapidomvp.presenter

import com.dtp.rapidomvp.state.State

/**
 * Created by ryantaylor on 9/26/16.
 */
interface stateManager {

    fun saveState(stateKey: String, state: State)

    fun <T : State> loadState(stateKey: String): T
}