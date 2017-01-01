package com.dtp.samplemvp.some

import com.dtp.rapido.mvp.presenter.BaseStatePresenter
import com.dtp.rapido.mvp.presenter.PresenterData

/**
 * Created by ner on 11/2/16.
 */
class SomePresenter(): BaseStatePresenter<SomeState, SomeView>() {

    override var view: SomeView? = null

    override var stateKey = "SomePresenter"

    private var something = "Nothing"

    override fun loadFromState(state: SomeState) {
        something = state.something
    }

    override fun saveState(presenterData: PresenterData) {
        presenterData.saveState(stateKey, SomeState(something))
    }
}