package com.izeni.rapidocommon.recycler

import android.util.SparseArray
import android.util.SparseIntArray
import android.view.ViewGroup
import com.izeni.rapidocommon.d

/**
 * Created by ner on 1/11/17.
 */
class SectionManager(private val adapter: MultiViewHolderAdapter, private val sections: List<MultiViewHolderAdapter.Section<*>>) {

    private val sectionEnds = SparseArray<Pair<Int, Int>>()
    private val headers = SparseIntArray()

    val count: Int
        get() {
            val count = sections.sumBy { it.count + it.headerCount }
            d("Count $count")

            return count
        }

    init {
        updatePositions()
    }

    private fun adjustPosition(position: Int): Int {
        var offset = (0..headers.size() - 1).count { headers.valueAt(it) < position }

        (0..sectionEnds.size() - 1)
                .map { sectionEnds.valueAt(it) }
                .filter { it.second < position }
                .forEach { offset += (it.second - it.first + 1) }

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

    fun getHeaderSectionType(position: Int): Int {
        (0..headers.size() - 1)
                .filter { position == headers.valueAt(it) }
                .forEach {
                    return headers.keyAt(it)
                }

        return -1
    }

    fun getSectionType(position: Int): Int {
        for (i in 0..sectionEnds.size() - 1) {
            val value = sectionEnds.valueAt(i)

            if (position >= value.first && position <= value.second)
                return sectionEnds.keyAt(i)
        }

        return -1
    }

    fun getSectionForType(sectionType: Int): MultiViewHolderAdapter.Section<*> {
        return sections.first { it.type == sectionType }
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
            val listEnd = sectionEnds.get(sectionType).second

            it.addItem(item)

            updatePositions()

            adapter.notifyItemInserted(listEnd + 1)
        }
    }

    private fun updatePositions() {
        var offset = 0

        sections.forEach { section ->
            if (section.hasHeader)
                headers.put(section.type, 0 + offset)

            sectionEnds.put(section.type, section.headerCount + offset to (section.count - 1) + section.headerCount + offset)

            offset += section.count + section.headerCount
        }
    }
}