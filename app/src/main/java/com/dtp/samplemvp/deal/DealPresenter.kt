package com.dtp.samplemvp.deal

import android.util.Log
import com.dtp.samplemvp.common.BaseCallback
import com.dtp.samplemvp.common.database.Deal
import com.dtp.samplemvp.common.network.DealService
import com.dtp.simplemvp.mvp.presenter.BaseStatePresenter

/**
 * Created by ryantaylor on 9/22/16.
 */
class DealPresenter(override var view: DealView?) : BaseStatePresenter<DealState, DealView>(){

    override var stateKey = "DealState"
    override lateinit var state: DealState

    val dealService = DealService()

    override fun newState(): DealState {
        return DealState()
    }

    override fun load() {
        Log.i("DealPresenter", "Loading new state")

        getDeals()
    }

    override fun reload() {
        Log.i("DealPresenter", "Loading from state")
        getDeals()

        state.newText?.let { view?.displayNewText(it) }
    }

    override fun loadFromSavedState() {
        Log.i("DealPresenter", "Loading from saved state")
        getDeals()

        state.newText?.let { view?.displayNewText(it) }
    }

    fun setNewText(text: String) {
        state.newText = text

        view?.displayNewText(text)
    }

    private fun getDeals() {
        dealService.getDeal(object : BaseCallback<Deal> {
            override fun failed(message: String?) {
                message?.let { view?.displayError(it) }
            }

            override fun succeeded(item: Deal) {

            }
        })
    }

    private fun loadNewText() {

    }
}