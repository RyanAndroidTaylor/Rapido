package com.dtp.samplemvp.deal

import com.dtp.samplemvp.common.BaseCallback
import com.dtp.samplemvp.common.database.Deal
import com.dtp.samplemvp.common.network.DealService
import com.dtp.simplemvp.mvp.presenter.BasePresenter

/**
 * Created by ryantaylor on 9/22/16.
 */
class DealPresenter(val view: DealView) : BasePresenter<DealState>(){
    override var stateKey = "DealState"
    override lateinit var state: DealState

    val dealService = DealService()

    override fun newState(): DealState {
        return DealState()
    }

    override fun onStateLoaded() {
        dealService.getDeal(object : BaseCallback<Deal> {
            override fun failed(message: String?) {
                message?.let { view.displayError(it) }
            }

            override fun succeeded(item: Deal) {

            }
        })
    }
}