package com.izeni.rapidocommon

import android.support.v7.util.DiffUtil
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * HelloEats
 * Created on 10/24/16.
 */
abstract class SimpleDiffable<in T>() {

    fun getDiffObservable(oldList: List<T>, newList: List<T>): Observable<DiffUtil.DiffResult> = Observable
            .just(DiffUtil.calculateDiff(DiffCallback(oldList, newList, { n, o -> areItemsTheSame(n, o) }, { n, o -> areContentsTheSame(n, o) }), false))
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())

    abstract fun areItemsTheSame(oldItem: T, newItem: T): Boolean
    abstract fun areContentsTheSame(oldItem: T, newItem: T) : Boolean

    class DiffCallback<T>(val oldList: List<T>, val newList: List<T>,
                          val areItemsTheSame:(T, T)->Boolean,
                          val areContentsTheSame:(T, T)->Boolean) : DiffUtil.Callback() {

        override fun getOldListSize() = oldList.size
        override fun getNewListSize() = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
                areItemsTheSame(oldList[oldItemPosition], newList[newItemPosition])

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
                areContentsTheSame(oldList[oldItemPosition], newList[newItemPosition])

    }
}