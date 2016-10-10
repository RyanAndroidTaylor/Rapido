package com.dtp.samplemvp.common.network

import com.dtp.rapido.database.DataConnection
import com.dtp.samplemvp.common.BaseCallback
import com.dtp.samplemvp.common.Const
import com.dtp.samplemvp.common.database.Deal
import com.dtp.samplemvp.common.network.responses.DealResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by ryantaylor on 9/26/16.
 */
class DealService {

    fun getDeal(onComplete: BaseCallback<Deal>) {
        NetworkClient.dealApi.getDeal(Const.MEH_API_KEY).enqueue(object : Callback<DealResponse> {
            override fun onResponse(call: Call<DealResponse>?, response: Response<DealResponse>?) {
                if (response != null && response.body() != null) {
                    DataConnection.save(response.body().deal)

                    onComplete.succeeded(response.body().deal)
                } else {
                    onComplete.failed("Bad request")
                }
            }

            override fun onFailure(call: Call<DealResponse>?, t: Throwable?) {
                onComplete.failed(t?.message)
            }

        })
    }
}