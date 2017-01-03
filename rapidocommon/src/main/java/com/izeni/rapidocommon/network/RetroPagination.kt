@file:Suppress("unused")

package com.izeni.rapidocommon.network

import android.net.Uri
import com.izeni.rapidocommon.network.NetworkCallback
import com.izeni.rapidocommon.network.SimpleNetworkCallback
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.ArrayList


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
fun <T> Observable<PagedObject<T>>.getPage(callback: NetworkCallback<PagedResult<T>>) =
        subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            val nextPage = if (it.next.isNullOrBlank()) null else Uri.parse(it.next).getQueryParameter("page")?.toInt()
                            callback.onSuccess(PagedResult(it.results, nextPage))
                        },
                        {
                            if (it is HttpException) callback.onError(it) else callback.onFatal(it)
                        }
                )!!

class PagedResult<out T>(val results: List<T>, val nextPage: Int?)
fun <T> Observable<PagedObject<T>>.pagedListTransformer() = map { it.results }!!

class PagedObject<T> {
    var count: Int = 0
    var next: String? = null
    var previous: String? = null
    var results: List<T> = ArrayList()
}

class PageHandler<T>(pageInterface: (Int) -> Observable<PagedObject<T>>) {
    var nextPage: Int? = 0
        private set

    private var pageObservable: (Int) -> Observable<PagedObject<T>>

    init {
        pageObservable = pageInterface
    }

    fun modifyPageObservable(pageInterface: (Int) -> Observable<PagedObject<T>>, reset: Boolean) {
        pageObservable = pageInterface
        if (reset) reset()
    }

    fun reset() {
        nextPage = 0
    }

    fun getNextPage(onSuccess: (List<T>) -> Unit, onError: (Throwable) -> Unit) {
        getSpecificPage(nextPage, onError) {
            val newList: MutableList<T> = arrayListOf()

            newList.addAll(it.results)
            nextPage = it.nextPage
            onSuccess(newList)
        }
    }


    private fun getSpecificPage(page: Int?, onError: (Throwable) -> Unit, onNext: (PagedResult<T>) -> Unit) {
        if (page != null) {
            pageObservable(page).getPage(
                    object : SimpleNetworkCallback<PagedResult<T>>() {
                        override fun onError(exception: HttpException) {
                            onError(exception)
                        }

                        override fun onFatal(exception: Throwable) {
                            onError(exception)
                        }

                        override fun onSuccess(data: PagedResult<T>) {
                            onNext(data)
                        }
                    })
        }
    }

    fun getAllPages(onSuccess: (List<T>) -> Unit, onError: (Throwable) -> Unit, pageStart: Int = 0) {
        val newList: MutableList<T> = arrayListOf()
        getPageRecursive(newList, pageStart, onSuccess, onError)
    }

    private fun getPageRecursive(list: MutableList<T>, page: Int?, onSuccess: (List<T>) -> Unit, onError: (Throwable) -> Unit) {
        getSpecificPage(page, onError) {
            list.addAll(it.results)

            if (it.nextPage == null) {
                nextPage = it.nextPage
                onSuccess(list)
            } else {
                getPageRecursive(list, it.nextPage, onSuccess, onError)
            }
        }
    }
}