package com.izeni.rapidocommon.recycler

import android.util.SparseArray
import android.util.SparseIntArray
import android.view.ViewGroup
import com.izeni.rapidocommon.d
import com.izeni.rapidocommon.e
import com.izeni.rapidocommon.recycler.MultiViewHolderAdapter.Companion.HEADER

/**
 * Created by ner on 1/11/17.
 */
class SectionManager(private val adapter: MultiViewHolderAdapter, private val sections: List<MultiViewHolderAdapter.Section<*>>) {

    private val sectionEnds = SparseArray<SectionSpan>()
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

        (0..sectionEnds.size() - 1)
                .map { sectionEnds.valueAt(it) }
                .filter { it.isBefore(position) }
                .forEach { offset += it.count }

        return position - offset
    }

    fun getViewHolderType(position: Int): Int {
        var viewHolderType = getSectionType(position)

        if (viewHolderType == -1) {
            (0..headers.size() - 1)
                    .filter { position == headers.valueAt(it) }
                    .forEach { viewHolderType = MultiViewHolderAdapter.HEADER }
        }

        return viewHolderType
    }

    fun createViewHolder(parent: ViewGroup, sectionType: Int): ViewHolder<*> {
        return getSectionForType(sectionType).viewHolderData.createViewHolder(parent)
    }

    fun getSectionType(position: Int): Int {
        for (i in 0..sectionEnds.size() - 1) {
            sectionEnds.valueAt(i).let {
                if (it.contains(position))
                    return sectionEnds.keyAt(i)
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

    fun getSectionForType(sectionType: Int): MultiViewHolderAdapter.Section<*> {
        return sections.first { it.type == sectionType }
    }

    fun hasHeaders(): Boolean {
        sections.forEach {
            if (it.hasHeader)
                return true
        }

        return false
    }

    fun bind(viewHolder: ViewHolder<*>, position: Int) {
        getSectionForType(getSectionType(position)).bind(viewHolder, adjustPosition(position))
    }

    @Suppress("UNCHECKED_CAST")
    fun bindHeader(viewHolder: ViewHolder<*>, position: Int) {
        getSectionForType(getHeaderSectionType(position)).bindHeader(viewHolder)
    }

    fun addItem(sectionType: Int, item: Any) {
        getSectionForType(sectionType).let {
            val listEnd = sectionEnds.get(sectionType).end

            it.addItem(item)

            updatePositions()

            adapter.notifyItemInserted(listEnd + 1)
        }
    }

    fun addItemAt(index: Int, sectionType: Int, item: Any) {
        getSectionForType(sectionType).let {
            val listStart = sectionEnds.get(sectionType).start

            it.addItemAt(index, item)

            updatePositions()

            adapter.notifyItemInserted(listStart + index)
        }
    }

    fun addItems(sectionType: Int, items: List<Any>) {
        getSectionForType(sectionType).let {
            val listEnd = sectionEnds.get(sectionType).end

            it.addItems(items)

            updatePositions()

            adapter.notifyItemRangeChanged(listEnd + 1, listEnd + 1 + items.size)
        }
    }

    fun removeItem(sectionType: Int, item: Any) {
        getSectionForType(sectionType).let {
            val listStart = sectionEnds.get(sectionType).start

            val index = it.removeItem(item)

            updatePositions()

            adapter.notifyItemRemoved(listStart + index)
        }
    }

    private fun updatePositions() {
        var offset = 0

        sections.forEach { section ->
            if (section.hasHeader)
                headers.put(section.type, 0 + offset)

            sectionEnds.put(section.type, SectionSpan(section.headerCount + offset, (section.count - 1) + section.headerCount + offset))

            offset += section.count + section.headerCount
        }
    }

    private class SectionSpan(val start: Int, val end: Int) {

        val count: Int
            get() =  end - start + 1

        fun contains(position: Int): Boolean = position >= start && position <= end

        fun isBefore(position: Int): Boolean = end < position

        fun isAfter(position: Int): Boolean = start > position
    }
}