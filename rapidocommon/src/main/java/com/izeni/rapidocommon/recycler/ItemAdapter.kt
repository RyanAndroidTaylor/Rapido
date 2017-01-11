package com.izeni.rapidocommon.recycler

import android.support.annotation.IdRes
import android.view.View
import android.view.ViewGroup
import com.izeni.rapidocommon.view.inflate

/**
 * Created by ner on 1/2/17.
 */
abstract class ItemAdapter<T>(items: MutableList<T>,
                              @IdRes val layoutId: Int,
                              val viewHolder: (View) -> ViewHolder<T>) :
        BaseAdapter<T>(items) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder<T> {
        return viewHolder(parent.inflate(layoutId))
    }
}