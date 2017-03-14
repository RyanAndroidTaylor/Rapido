package com.dtp.sample.sectioned_recycler

import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.dtp.sample.R
import com.izeni.rapidocommon.recycler.MultiTypeSectionedAdapter
import com.izeni.rapidocommon.recycler.SectionManager
import com.izeni.rapidocommon.recycler.SectionedViewHolder
import com.izeni.rapidocommon.view.bind
import com.izeni.rapidocommon.view.onClick

/**
 * Created by ner on 1/31/17.
 */
class SectionedAdapter(sections: List<Section<*>>, val expandSection: (Boolean, Int) -> Unit) :
        MultiTypeSectionedAdapter(sections, SectionedViewHolderData(R.layout.item_sample_section, { SectionViewHolder(it, expandSection) })) {

    class SectionViewHolder(view: View, val expand: (Boolean, Int) -> Unit) : SectionedViewHolder<SectionManager.SectionData>(view) {

        val title by bind<TextView>(R.id.section_title)
        val count by bind<TextView>(R.id.section_item_count)
        val chevron by bind<ImageView>(R.id.section_item_chevron)

        override fun bind(item: SectionManager.SectionData) {

            chevron.onClick { expand(item.isCollapsed, item.type) }

            when (item.type) {
                0 -> title.text = "String"
                1 -> title.text = "Int"
                2 -> title.text = "Pair<String, String>"
            }

            count.text = "${item.sectionCount}"

            if (item.isCollapsed)
                chevron.setImageDrawable(ContextCompat.getDrawable(itemView.context, R.drawable.ic_chevron_right))
            else
                chevron.setImageDrawable(ContextCompat.getDrawable(itemView.context, R.drawable.ic_chevron_down))

        }
    }
}