package com.dtp.simplemvp.mvp.state

import java.util.*

/**
 * Created by ryantaylor on 9/24/16.
 */
object StateManager {

    private val states = HashMap<String, State>()

    /**
     * @param key The key used to reference the state
     * @return Returns true if StateManager is storing a state under "key"
     */
    fun hasState(key: String): Boolean {
        return states.containsKey(key)
    }

    /**
     * Adds a state to be held onto
     *
     * @param key The key to store the state under
     * @param state The state to store
     */
    fun addState(key: String, state: State) {
        states.put(key, state)
    }

    /**
     * Returns the state stored under "key". You should always check if the state exists with before calling
     * this method. @see[hasState]
     */
    fun <T: State> getState(key: String): T {
        return states[key]!! as T
    }

    /**
     * Removes the state stored under key.
     *
     * @param key The key of the state that you want to remove
     */
    fun removeState(key: String) {
        states.remove(key)
    }
}