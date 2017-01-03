package com.izeni.rapidocommon

import android.support.annotation.LayoutRes
import android.util.SparseArray
import android.view.View
import android.view.ViewGroup

/**
 * Created by ner on 1/2/17.
 */
abstract class MultiViewHolderAdapter(items: MutableList<AdapterItem<Any>>, vararg viewHolders: ViewHolderData) : ItemAdapter<MultiViewHolderAdapter.AdapterItem<Any>>(items) {

    private val holders = SparseArray<ViewHolderData>()

    init {
        viewHolders.forEach { holders.put(it.layoutId, it) }
    }

    override fun getItemViewType(position: Int): Int {
        return items[position].viewHolderId
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder<AdapterItem<Any>> {
        return holders[viewType]!!.createViewHolder(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder<AdapterItem<Any>>, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    abstract class AdapterItem<out T>(val data: T) {
        abstract val viewHolderId: Int
    }

    class ViewHolderData(@LayoutRes val layoutId: Int, val viewHolder: (View) -> ViewHolder<AdapterItem<Any>>) {

        fun createViewHolder(parent: ViewGroup): ViewHolder<AdapterItem<Any>> {
            return viewHolder(parent.inflate(layoutId))
        }
    }
}