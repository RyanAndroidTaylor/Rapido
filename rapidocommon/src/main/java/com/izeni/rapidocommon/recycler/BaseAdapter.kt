package com.izeni.rapidocommon.recycler

import android.support.v7.widget.RecyclerView

/**
 * Created by ner on 1/11/17.
 */
abstract class BaseAdapter<T>(val items: MutableList<T>) : RecyclerView.Adapter<ViewHolder<T>>() {

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder<T>, position: Int) {
        holder.bind(items[position])
    }

    fun addItem(item: T) {
        items.add(item)

        notifyItemChanged(items.size - 1)
    }

    fun addItems(newItems: List<T>) {
        val startPosition = items.size + 1

        items.addAll(newItems)

        notifyItemRangeChanged(startPosition, newItems.size)
    }
}