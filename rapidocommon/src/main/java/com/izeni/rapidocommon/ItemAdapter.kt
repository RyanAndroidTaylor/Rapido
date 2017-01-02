package com.izeni.rapidocommon

import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.util.SparseArray
import android.view.View
import android.view.ViewGroup

/**
 * Created by ner on 1/2/17.
 */
class ItemAdapter(val items: MutableList<AdapterItem<Any>>, vararg viewHolders: ViewHolderData) : RecyclerView.Adapter<ItemAdapter.ViewHolder>() {

    private val holders = SparseArray<ViewHolderData>()

    init {
        viewHolders.forEach { holders.put(it.layoutId, it) }
    }

    override fun getItemViewType(position: Int): Int {
        return items[position].viewHolderId
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return holders[viewType]!!.createViewHolder(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    abstract class AdapterItem<out T>(val data: T) {
        abstract val viewHolderId: Int
    }

    class ViewHolderData(@LayoutRes val layoutId: Int, val viewHolder: (View) -> ViewHolder) {

        fun createViewHolder(parent: ViewGroup): ViewHolder {
            return viewHolder(parent.inflate(layoutId))
        }
    }

    abstract class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        abstract fun bind(item: AdapterItem<Any>)
    }
}