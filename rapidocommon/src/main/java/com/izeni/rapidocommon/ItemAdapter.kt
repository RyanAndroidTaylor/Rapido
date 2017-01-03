package com.izeni.rapidocommon

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup

/**
 * Created by ner on 1/2/17.
 */
abstract class ItemAdapter<T>(val items: MutableList<T>) : RecyclerView.Adapter<ItemAdapter.ViewHolder<T>>() {

    abstract override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder<T>

    override fun onBindViewHolder(holder: ViewHolder<T>, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    abstract class ViewHolder<in T>(view: View) : RecyclerView.ViewHolder(view) {
        abstract fun bind(item: T)
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