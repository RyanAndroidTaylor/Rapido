package com.izeni.rapidocommon.recycler

import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager

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
class PaginationRecyclerListener(val onLoadMore:((offset: Int)->Unit)?, var bufferSize: Int = 10): RecyclerView.OnScrollListener() {
    var updating = false
    var endOfList = false

    override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        if (!updating && !endOfList) {
            val layoutManger = recyclerView?.layoutManager

            if (layoutManger != null) {
                val totalItemCount = layoutManger.itemCount

                val lastVisiblePosition = when(layoutManger) {
                    is GridLayoutManager -> layoutManger.findLastVisibleItemPosition()
                    is StaggeredGridLayoutManager -> layoutManger.findLastVisibleItemPositions(null).max() ?: -1
                    is LinearLayoutManager -> layoutManger.findLastVisibleItemPosition()
                    else -> return
                }

                if (bufferSize < totalItemCount && lastVisiblePosition >= totalItemCount - bufferSize) {
                    onLoadMore?.let {
                        it.invoke(totalItemCount)
                        updating = true
                    }
                }
            }
        }
    }

    fun loadingFinished(endOfList: Boolean = false) {
        updating = false
        this.endOfList = endOfList
    }
}