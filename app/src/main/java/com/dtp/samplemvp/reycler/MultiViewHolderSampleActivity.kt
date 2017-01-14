package com.dtp.samplemvp.reycler

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.dtp.samplemvp.R
import com.izeni.rapidocommon.recycler.MultiViewHolderAdapter
import com.izeni.rapidocommon.recycler.MultiViewHolderAdapter.Section
import com.izeni.rapidocommon.recycler.MultiViewHolderAdapter.SectionedViewHolderData
import com.izeni.rapidocommon.recycler.SectionManager.SectionData
import com.izeni.rapidocommon.recycler.SectionedViewHolder
import com.izeni.rapidocommon.view.bind
import com.izeni.rapidocommon.view.onClick
import kotlinx.android.synthetic.main.activity_multi_view_holder_sample.*
import java.util.*

/**
 * Created by ner on 1/11/17.
 */
class MultiViewHolderSampleActivity : AppCompatActivity() {

    val recyclerView  by bind<RecyclerView>(R.id.multi_view_holder_recycler)

    var adapter: SampleAdapter? = null

    val random = Random()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_multi_view_holder_sample)

        add_item.onClick {
            val type = random.nextInt(3)
            adapter?.addItem(type, getItem(type))
        }
        add_item_at.onClick {
            val type = random.nextInt(3)
            adapter?.addItemAt(4, type, getItem(type))
        }

        val sectionOne = object : Section<String>(
                0,
                mutableListOf("One", "Two", "Three", "Four", "Five", "Six"),
                SectionedViewHolderData(R.layout.item_sample_one, { SampleViewHolderOne(it) }),
                { type, item -> adapter?.removeItem(type, item) },
                hasHeader = true,
                isCollapsible = true) {}

        val sectionTwo = object : Section<Int>(
                1,
                mutableListOf(1, 2, 3, 4, 5, 6),
                SectionedViewHolderData(R.layout.item_sample_two, { SampleViewHolderTwo(it) }),
                { type, item -> adapter?.removeItem(type, item) },
                hasHeader = true,
                isCollapsible = true) {}

        val sectionThree = object : Section<Pair<String, String>>(
                2,
                mutableListOf("One" to "1", "Two" to "2", "Three" to "3", "Four" to "4", "Five" to "5", "Six" to "6"),
                SectionedViewHolderData(R.layout.item_sample_three, { SampleViewHolderThree(it) }),
                { type, item -> adapter?.removeItem(type, item) },
                hasHeader = true,
                isCollapsible = true) {}

        adapter = SampleAdapter(listOf(sectionOne, sectionTwo, sectionThree),
                                { isCollapsed, type ->
                                    if (isCollapsed)
                                        adapter?.expandSection(type)
                                    else
                                        adapter?.collapseSection(type)
                                })

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    private fun getItem(type: Int): Any {
        when (type) {
            0 -> return "${random.nextInt(100)}"
            1 -> return random.nextInt(100)
            2 -> return "${random.nextInt(100)}" to "${random.nextInt(100)}"
        }

        return "Added"
    }

    class SampleAdapter(sections: List<Section<*>>, val expand: (Boolean, Int) -> Unit) :
            MultiViewHolderAdapter(sections,
                                   SectionedViewHolderData(R.layout.item_sample_section, { SectionViewHolder(it, expand) }))

    class SectionViewHolder(view: View, val expand: (Boolean, Int) -> Unit) : SectionedViewHolder<SectionData>(view) {

        val title by bind<TextView>(R.id.section_title)
        val count by bind<TextView>(R.id.section_item_count)
        val chevron by bind<ImageView>(R.id.section_item_chevron)

        override fun bind(item: SectionData) {

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

    class SampleViewHolderOne(view: View) : SectionedViewHolder<String>(view) {

        val text by bind<TextView>(R.id.sample_item_text)

        override fun bind(item: String) {
            itemView.onClick { onClick?.invoke(0, item) }

            text.text = item
        }
    }

    class SampleViewHolderTwo(view: View) : SectionedViewHolder<Int>(view) {

        val text by bind<TextView>(R.id.sample_item_two_text)

        override fun bind(item: Int) {
            itemView.onClick { onClick?.invoke(1, item) }

            text.text = "$item"
        }
    }

    class SampleViewHolderThree(view: View) : SectionedViewHolder<Pair<String, String>>(view) {

        val textOne by bind<TextView>(R.id.item_sample_three_text_one)
        val textTwo by bind<TextView>(R.id.item_sample_three_text_two)

        override fun bind(item: Pair<String, String>) {
            itemView.onClick { onClick?.invoke(2, item) }

            textOne.text = item.first
            textTwo.text = item.second
        }
    }
}