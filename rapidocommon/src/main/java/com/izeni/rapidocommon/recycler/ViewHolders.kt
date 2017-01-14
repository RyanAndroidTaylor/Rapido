package com.izeni.rapidocommon.recycler

import android.support.v7.widget.RecyclerView
import android.view.View

/**
 * Created by ner on 1/11/17.
 */
abstract class SectionedViewHolder<T>(view: View) : RecyclerView.ViewHolder(view) {
    var onClick: ((Int, T) -> Unit)? = null

    abstract fun bind(item: T)
}

abstract class ViewHolder<T>(view: View) : RecyclerView.ViewHolder(view) {
    var onClick: ((T) -> Unit)? = null

    abstract fun bind(item: T)
}