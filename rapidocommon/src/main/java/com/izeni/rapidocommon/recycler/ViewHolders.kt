package com.izeni.rapidocommon.recycler

import android.support.v7.widget.RecyclerView
import android.view.View

/**
 * Created by ner on 1/11/17.
 */
abstract class ViewHolder<in T>(view: View) : RecyclerView.ViewHolder(view) {
    abstract fun bind(item: T)
}