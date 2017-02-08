package com.izeni.rapidocommon.recycler

import android.support.annotation.IdRes
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.izeni.rapidocommon.view.inflate

/**
 * Created by ner on 1/2/17.
 */
abstract class ItemAdapter<T>(val items: MutableList<T>,
                              @IdRes val layoutId: Int,
                              val viewHolder: (View) -> ViewHolder<T>) :
        RecyclerView.Adapter<ViewHolder<T>>() {

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder<T>, position: Int) {
        holder.bind(items[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder<T> {
        return viewHolder(parent.inflate(layoutId))
    }

    open fun addItem(item: T) {
        items.add(item)

        notifyItemChanged(items.size - 1)
    }

    open fun addItems(newItems: List<T>) {
        val startPosition = items.size

        items.addAll(newItems)

        notifyItemRangeChanged(startPosition, newItems.size)
    }
}