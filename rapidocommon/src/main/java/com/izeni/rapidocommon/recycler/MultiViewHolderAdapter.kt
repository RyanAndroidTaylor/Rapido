package com.izeni.rapidocommon.recycler

import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.izeni.rapidocommon.view.inflate

/**
 * Created by ner on 1/2/17.
 */
abstract class MultiViewHolderAdapter(sections: List<Section<*>>, val sectionHeader: ViewHolderData<HeaderData>? = null) : RecyclerView.Adapter<ViewHolder<*>>() {

    companion object {
        val HEADER = 0
    }

    private val sectionManager by lazy { SectionManager(this, sections) }

    override fun getItemViewType(position: Int): Int {
        return sectionManager.getViewHolderType(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder<*> {
        if (viewType == HEADER)
            return sectionHeader!!.createViewHolder(parent)
        else
            return sectionManager.createViewHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: ViewHolder<*>, position: Int) {
        val type = getItemViewType(position)

        if (type == HEADER)
            sectionManager.bindHeader(holder, position)
        else
            sectionManager.bind(holder, position)
    }

    override fun getItemCount() = sectionManager.count

    fun addItem(sectionType: Int, item: Any) {
        sectionManager.addItem(sectionType, item)
    }

    class ViewHolderData<in T>(@LayoutRes val layoutId: Int, val viewHolder: (View) -> ViewHolder<T>) {
        fun createViewHolder(parent: ViewGroup): ViewHolder<T> {
            return viewHolder(parent.inflate(layoutId))
        }
    }

    class HeaderData(val type: Int, val sectionCount: Int)

    abstract class Section<T>(val type: Int, val items: MutableList<T>, val viewHolderData: ViewHolderData<T>, val hasHeader: Boolean = false) {

        val count: Int
            get() = items.size

        val headerCount = if (hasHeader) 1 else 0

        @Suppress("UNCHECKED_CAST")
        fun bind(viewHolder: ViewHolder<*>, position: Int) {
            (viewHolder as ViewHolder<T>).bind(items[position])
        }

        @Suppress("UNCHECKED_CAST")
        fun bindHeader(viewHolder: ViewHolder<*>) {
            (viewHolder as ViewHolder<HeaderData>).bind(HeaderData(type, items.size))
        }

        @Suppress("UNCHECKED_CAST")
        fun addItem(item: Any) {
            items.add(item as T)
        }
    }
}