package com.izeni.rapidocommon.recycler

import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.izeni.rapidocommon.view.inflate
import com.izeni.rapidocommon.recycler.SectionManager.SectionData

/**
 * Created by ner on 1/2/17.
 */
//TODO Need to make it so section headers span the recycler view when using a GridLayoutManager
abstract class MultiViewSectionedAdapter(sections: List<Section<*>>,
                                         private val sectionHeader: SectionedViewHolderData<SectionData>? = null,
                                         onClick: ((Int, SectionData) -> Unit)? = null) :
        RecyclerView.Adapter<SectionedViewHolder<*>>() {

    companion object {
        val HEADER = -112
    }

    private val sectionManager by lazy { SectionManager(this, sections) }

    init {
        if (sectionHeader == null && sectionManager.hasHeaders())
            throw IllegalStateException("One of your sections has a header but there was no SectionedViewHolderData passed for section headers")

        sectionHeader?.onClick = onClick
    }

    override fun getItemViewType(position: Int): Int {
        return sectionManager.getViewHolderType(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SectionedViewHolder<*> {
        if (viewType == HEADER)
            return sectionHeader!!.createViewHolder(parent)
        else
            return sectionManager.createViewHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: SectionedViewHolder<*>, position: Int) {
        val type = getItemViewType(position)

        if (type == HEADER)
            sectionManager.bindHeader(holder, position)
        else
            sectionManager.bind(holder, position)
    }

    override fun getItemCount() = sectionManager.count

    fun collapseSection(sectionType: Int) {
        sectionManager.collapseSection(sectionType)
    }

    fun expandSection(sectionType: Int) {
        sectionManager.expandSection(sectionType)
    }


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

    class SectionedViewHolderData<T>(@LayoutRes val layoutId: Int, val viewHolder: (View) -> SectionedViewHolder<T>) {
        var onClick: ((Int, T) -> Unit)? = null

        fun createViewHolder(parent: ViewGroup): SectionedViewHolder<T> {
            return viewHolder(parent.inflate(layoutId)).apply { this.onClick = this@SectionedViewHolderData.onClick }
        }
    }

    @Suppress("UNCHECKED_CAST")
    abstract class Section<T>(val type: Int,
                              private val items: MutableList<T>,
                              val viewHolderData: SectionedViewHolderData<T>,
                              onClick: ((Int, T) -> Unit)? = null,
                              val hasHeader: Boolean = false,
                              val isCollapsible: Boolean = false) {

        init {
            viewHolderData.onClick = onClick
        }

        val count: Int
            get() = if (isCollapsed) 0 else items.size

        val headerCount = if (hasHeader) 1 else 0

        var isCollapsed = false
            set(value) { if (isCollapsible) field = value }

        fun bind(viewHolder: SectionedViewHolder<*>, position: Int) {
            (viewHolder as SectionedViewHolder<T>).bind(items[position])
        }

        fun bindHeader(viewHolder: SectionedViewHolder<*>) {
            (viewHolder as SectionedViewHolder<SectionData>).bind(SectionData(type, items.size, isCollapsed))
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