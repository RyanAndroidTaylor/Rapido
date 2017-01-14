package com.izeni.rapidocommon.recycler

import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.izeni.rapidocommon.view.inflate

/**
 * Created by ner on 1/2/17.
 */
abstract class MultiViewHolderAdapter(sections: List<Section<*>>, private val sectionHeader: ViewHolderData<SectionData>? = null) : RecyclerView.Adapter<ViewHolder<*>>() {

    companion object {
        val HEADER = -112
    }

    private val sectionManager by lazy { SectionManager(this, sections) }

    init {
        if (sectionHeader == null && sectionManager.hasHeaders())
            throw IllegalStateException("One of your sections has a header but there was no ViewHolderData passed for section headers")
    }

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

    fun addItemAt(index: Int, sectionType: Int, item: Any) {
        sectionManager.addItemAt(index, sectionType, item)
    }

    fun addItems(sectionType: Int, items: List<Any>) {
        sectionManager.addItems(sectionType, items)
    }

    fun removeItem(sectionType: Int, item: Any) {
        sectionManager.removeItem(sectionType, item)
    }

    class ViewHolderData<T>(@LayoutRes val layoutId: Int, val viewHolder: (View) -> ViewHolder<T>) {
        var onClick: ((Int, T) -> Unit)? = null

        fun createViewHolder(parent: ViewGroup): ViewHolder<T> {
            return viewHolder(parent.inflate(layoutId)).apply { this.onClick = this@ViewHolderData.onClick }
        }
    }

    class SectionData(val type: Int, val sectionCount: Int)

    @Suppress("UNCHECKED_CAST")
    abstract class Section<T>(val type: Int,
                              private val items: MutableList<T>,
                              val viewHolderData: ViewHolderData<T>,
                              onClick: ((Int, T) -> Unit)? = null,
                              val hasHeader: Boolean = false) {

        init {
            viewHolderData.onClick = onClick
        }

        val count: Int
            get() = items.size

        val headerCount = if (hasHeader) 1 else 0

        fun bind(viewHolder: ViewHolder<*>, position: Int) {
            (viewHolder as ViewHolder<T>).bind(items[position])
        }

        fun bindHeader(viewHolder: ViewHolder<*>) {
            (viewHolder as ViewHolder<SectionData>).bind(SectionData(type, items.size))
        }

        fun addItem(item: Any) {
            items.add(item as T)
        }

        fun addItemAt(index: Int, item: Any) {
            items.add(index, item as T)
        }

        fun addItems(items: List<Any>) {
            this.items.addAll(items as List<T>)
        }

        fun removeItem(item: Any): Int {
            val index = items.indexOf(item as T)

            items.removeAt(index)

            return index
        }
    }
}