package com.dtp.samplemvp.common.network

import com.dtp.samplemvp.common.network.responses.DealResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by ryantaylor on 9/26/16.
 */
object NetworkClient {
    val BASE_URL = "https://api.meh.com/1/"

    val dealApi = buildDealApi()

    private fun buildDealApi(): DealApi {
        val client = OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().apply { this.level = HttpLoggingInterceptor.Level.BODY })
                .build()

        return Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
                .create(DealApi::class.java)
    }

    interface DealApi {

        @GET("current.json")
        fun getDeal(@Query("apikey") apiKey: String): Call<DealResponse>
    }
}