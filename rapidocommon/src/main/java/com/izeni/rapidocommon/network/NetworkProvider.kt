@file:Suppress("unused")

package com.izeni.rapidocommon.network

import android.content.Context
import android.util.Base64
import com.squareup.picasso.Picasso
import com.izeni.rapidocommon.*
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import io.reactivex.disposables.CompositeDisposable
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.properties.Delegates

/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 Izeni, Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 **/
object NetworkProvider {
    class ApiChangeEvent

    fun httpClient() = OkHttpClient.Builder().apply {
        if(cacheSize > 0 && contextInitialized) {
            cache(Cache(network_context.cacheDir, cacheSize))
            addInterceptor { chain ->
                var request = chain.request()
                if (network_context.isNetworkAvalible()) {
                    request = request.newBuilder().header("Cache-Control", "public, max-age=" + 60).build()
                } else {
                    request = request.newBuilder().header("Cache-Control", "public, only-if-cached, max-stale=" + 60 * 60 * 24 * 7).build()
                }
                chain.proceed(request)
            }
        }
    }

    private var cacheSize: Long = 0
    private var network_context: Context by Delegates.notNull()
    private var contextInitialized: Boolean = false

//    val realmExclusionGson by lazy {
//        GsonBuilder()
//                .setExclusionStrategies(object : ExclusionStrategy {
//                    override fun shouldSkipClass(clazz: Class<*>?) = clazz?.declaringClass?.equals(RealmObject::class.java) ?: false
//                    override fun shouldSkipField(f: FieldAttributes?) = false
//                })
//
//    }

    private val picassoManager by lazy { ResettableLazyManager() }
    val picasso: Picasso by ResettableLazy(picassoManager) {
        if(!contextInitialized) throw IllegalStateException("Network context has not been initialized.  Make sure you call setNetworkContext or Panko.init before using this method.")

        Picasso.Builder(network_context).listener { picasso, uri, exception -> e("Picasso", t = exception) }.build()
    }

    fun setNetworkContext(context: Context) {
        network_context = context
        picassoManager.reset()
        contextInitialized = true
    }

    fun initCache(cacheSize: Long = (10 * 1024 * 1024)) {
        if(!contextInitialized) throw IllegalStateException("Network context has not been initialized.  Make sure you call setNetworkContext or Panko.init before creating the cache.")

        this.cacheSize = cacheSize
    }

    fun <S> createNoAuthService(serviceClass: Class<S>, baseURL: String): S {
        return createService(serviceClass, null, baseURL = baseURL)
    }

    fun <S> createBasicService(serviceClass: Class<S>, username: String, password: String, baseURL: String): S {
        val basic = Base64.encodeToString("$username:$password".toByteArray(), Base64.NO_WRAP)
        return createService(serviceClass, "Basic $basic", baseURL = baseURL)
    }

    fun <S> createService(serviceClass: Class<S>, token: String?, baseURL: String): S {
        httpClient().let { httpClient ->
            httpClient.addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })

            if (token != null) {
                httpClient.addInterceptor { chain ->
                    val original: Request = chain.request()
                    val request: Request = original.newBuilder()
                            .header("Accept", "application/json")
                            .header("Authorization", token)
                            .method(original.method(), original.body())
                            .build()

                    val response = chain.proceed(request)

                    response
                }
            }


            return Retrofit.Builder()
                    .client(httpClient.build())
                    .baseUrl(baseURL)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build().create(serviceClass)
        }
    }
}

abstract class NetworkInterface<out T> {
    protected val subscription by lazy { CompositeDisposable() }

    init {
        subscription.add(RxBus.toObservable().filter { it is NetworkProvider.ApiChangeEvent }.subscribe({ reset() }, {}))
    }

    protected val resetManager by lazy { ResettableLazyManager() }
    protected abstract fun createApi(): T

    val api: T by ResettableLazy(resetManager) { createApi() }

    fun reset() {
        resetManager.reset()
    }
}