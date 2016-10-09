package com.dtp.samplemvp.deal

import android.util.Log
import com.dtp.rapido.database.DataConnection
import com.dtp.rapido.database.query.Query
import com.dtp.rapido.database.query.QueryBuilder
import com.dtp.samplemvp.common.BaseCallback
import com.dtp.samplemvp.common.database.Deal
import com.dtp.samplemvp.common.network.DealService
import com.dtp.rapido.mvp.presenter.BaseStatePresenter

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

    override fun load() {getDeals()
    }

    override fun reload() {

    }

    private fun getDeals() {
        dealService.getDeal(object : BaseCallback<Deal> {
            override fun succeeded(item: Deal) {
                DataConnection.save(item)

                val loadedDeal = DataConnection.findFirst(Deal.BUILDER, QueryBuilder().from(Deal.TABLE_NAME).build())

                Log.i("DealPresenter", "Loaded deal $loadedDeal")
            }

            override fun failed(message: String?) {
                message?.let { view?.displayError(it) }
            }
        })
    }
}