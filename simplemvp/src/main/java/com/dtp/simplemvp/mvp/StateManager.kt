package com.dtp.simplemvp.mvp

import java.util.*

/**
 * Created by ryantaylor on 9/24/16.
 */
object StateManager {

    private val states = HashMap<String, State>()

    fun hasState(key: String): Boolean {
        return states.containsKey(key)
    }

    fun funAddState(key: String, state: State) {
        states.put(key, state)
    }

    fun getState(key: String): State {
        return states[key]!!
    }

    fun removeState(key: String) {
        states.remove(key)
    }
}