package com.izeni.rapidocommon.recycler

import android.util.SparseArray
import android.util.SparseIntArray
import android.view.ViewGroup
import com.izeni.rapidocommon.e

/**
 * Created by ner on 1/11/17.
 */
class SectionManager(private val adapter: MultiViewSectionedAdapter, private val sections: List<MultiViewSectionedAdapter.Section<*>>) {

    private val sectionSpans = SparseArray<SectionSpan>()
    private val headers = SparseIntArray()

    val count: Int
        get() = sections.sumBy { it.count + it.headerCount }

    init {
        updatePositions()

        val set = mutableSetOf<Int>()
        sections.forEach {
            if (set.contains(it.type))
                throw IllegalStateException("Two of your sections have a matching type. Types must be unique")
            else
                set.add(it.type)
        }
    }

    private fun adjustPosition(position: Int): Int {
        var offset = (0..headers.size() - 1).count { headers.valueAt(it) < position }

        (0..sectionSpans.size() - 1)
                .map { sectionSpans.valueAt(it) }
                .filter { it.isBefore(position) }
                .forEach { offset += it.count }

        return position - offset
    }

    private fun bindSectionHeader(type: Int) {
        headers.get(type, -1).let {
            if (it != -1)
                adapter.notifyItemChanged(it)
        }
    }

    private fun getSectionForType(sectionType: Int): MultiViewSectionedAdapter.Section<*> {
        return sections.first { it.type == sectionType }
    }

    private fun updatePositions() {
        var offset = 0

        sections.forEach { section ->
            if (section.hasHeader)
                headers.put(section.type, 0 + offset)

            sectionSpans.put(section.type, SectionSpan(section.headerCount + offset, (section.count - 1) + section.headerCount + offset))

            offset += section.headerCount

            if (!section.isCollapsed)
                offset += section.count
        }
    }

    fun getViewHolderType(position: Int): Int {
        var viewHolderType = getSectionType(position)

        if (viewHolderType == -1) {
            (0..headers.size() - 1)
                    .filter { position == headers.valueAt(it) }
                    .forEach { viewHolderType = MultiViewSectionedAdapter.HEADER }
        }

        return viewHolderType
    }

    fun createViewHolder(parent: ViewGroup, sectionType: Int): SectionedViewHolder<*> {
        return getSectionForType(sectionType).viewHolderData.createViewHolder(parent)
    }

    fun getSectionType(position: Int): Int {
        for (i in 0..sectionSpans.size() - 1) {
            sectionSpans.valueAt(i).let {
                if (it.contains(position))
                    return sectionSpans.keyAt(i)
            }
        }

        e("No type found for position $position")
        return -1
    }

    fun getHeaderSectionType(position: Int): Int {
        (0..headers.size() - 1)
                .filter { position == headers.valueAt(it) }
                .forEach { return headers.keyAt(it) }

        e("No header type found for position $position")
        return -1
    }

    fun hasHeaders(): Boolean {
        sections.forEach {
            if (it.hasHeader)
                return true
        }

        return false
    }

    fun bind(viewHolder: SectionedViewHolder<*>, position: Int) {
        getSectionForType(getSectionType(position)).bind(viewHolder, adjustPosition(position))
    }

    fun bindHeader(viewHolder: SectionedViewHolder<*>, position: Int) {
        getSectionForType(getHeaderSectionType(position)).bindHeader(viewHolder)
    }

    fun collapseSection(sectionType: Int) {
        sections.firstOrNull { it.type == sectionType }?.isCollapsed = true

            sectionSpans.get(sectionType)?.let {
                updatePositions()

                //TODO When using notifyItemRangeRemoved the sections are not being marked as collapsed
//                adapter.notifyItemRangeRemoved(it.start, it.end - it.start + 1)
                adapter.notifyDataSetChanged()
            }
    }

    fun expandSection(sectionType: Int) {
        sections.firstOrNull { it.type == sectionType }?.isCollapsed = false

        sectionSpans.get(sectionType)?.let {
            updatePositions()

//            adapter.notifyItemRangeInserted(it.start, it.end - it.start)
            adapter.notifyDataSetChanged()
        }
    }

    fun addItem(sectionType: Int, item: Any) {
        getSectionForType(sectionType).let {
            it.addItem(item)

            updatePositions()

            adapter.notifyItemInserted(sectionSpans.get(sectionType).end)

            bindSectionHeader(sectionType)
        }
    }

    fun addItemAt(index: Int, sectionType: Int, item: Any) {
        getSectionForType(sectionType).let {
            val listStart = sectionSpans.get(sectionType).start

            it.addItemAt(index, item)

            updatePositions()

            adapter.notifyItemInserted(listStart + index)

            bindSectionHeader(sectionType)
        }
    }

    fun addItems(sectionType: Int, items: List<Any>) {
        getSectionForType(sectionType).let {
            val listEnd = sectionSpans.get(sectionType).end

            it.addItems(items)

            updatePositions()

            adapter.notifyItemRangeChanged(listEnd + 1, listEnd + 1 + items.size)

            bindSectionHeader(sectionType)
        }
    }

    fun removeItem(sectionType: Int, item: Any) {
        getSectionForType(sectionType).let {
            val listStart = sectionSpans.get(sectionType).start

            val index = it.removeItem(item)

            updatePositions()

            adapter.notifyItemRemoved(listStart + index)

            bindSectionHeader(sectionType)
        }
    }

    class SectionData(val type: Int, val sectionCount: Int, val isCollapsed: Boolean)

    private class SectionSpan(val start: Int, val end: Int) {

        val count: Int
            get() = end - start + 1

        fun contains(position: Int): Boolean = position >= start && position <= end

        fun isBefore(position: Int): Boolean = end < position

        fun isAfter(position: Int): Boolean = start > position
    }
}