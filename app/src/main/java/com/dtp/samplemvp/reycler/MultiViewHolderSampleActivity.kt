package com.dtp.samplemvp.reycler

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.dtp.samplemvp.R
import com.izeni.rapidocommon.recycler.MultiViewHolderAdapter
import com.izeni.rapidocommon.recycler.MultiViewHolderAdapter.SectionData
import com.izeni.rapidocommon.recycler.MultiViewHolderAdapter.Section
import com.izeni.rapidocommon.recycler.MultiViewHolderAdapter.ViewHolderData
import com.izeni.rapidocommon.recycler.ViewHolder
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
                ViewHolderData(R.layout.item_sample_one, { SampleViewHolderOne(it) }),
                { type, item -> adapter?.removeItem(type, item) },
                true) {}

        val sectionTwo = object : Section<Int>(
                1,
                mutableListOf(1, 2, 3, 4, 5, 6),
                ViewHolderData(R.layout.item_sample_two, { SampleViewHolderTwo(it) }),
                { type, item -> adapter?.removeItem(type, item) }, true) {}

        val sectionThree = object : Section<Pair<String, String>>(
                2,
                mutableListOf("One" to "1", "Two" to "2", "Three" to "3", "Four" to "4", "Five" to "5", "Six" to "6"),
                ViewHolderData(R.layout.item_sample_three, { SampleViewHolderThree(it) }),
                { type, item -> adapter?.removeItem(type, item) },
                true) {}

        adapter = SampleAdapter(listOf(sectionOne, sectionTwo, sectionThree))

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

    class SampleAdapter(sections: List<Section<*>>) :
            MultiViewHolderAdapter(sections, ViewHolderData(R.layout.item_sample_section, { SectionViewHolder(it) }))

    class SectionViewHolder(view: View) : ViewHolder<SectionData>(view) {

        val title by bind<TextView>(R.id.section_title)
        val count by bind<TextView>(R.id.section_item_count)

        override fun bind(item: SectionData) {
            when (item.type) {
                0 -> title.text = "String"
                1 -> title.text = "Int"
                2 -> title.text = "Pair<String, String>"
            }

            count.text = "${item.sectionCount}"
        }
    }

    class SampleViewHolderOne(view: View) : ViewHolder<String>(view) {

        val text by bind<TextView>(R.id.sample_item_text)

        override fun bind(item: String) {
            itemView.onClick { onClick?.invoke(0, item) }

            text.text = item
        }
    }

    class SampleViewHolderTwo(view: View) : ViewHolder<Int>(view) {

        val text by bind<TextView>(R.id.sample_item_two_text)

        override fun bind(item: Int) {
            itemView.onClick { onClick?.invoke(1, item) }

            text.text = "$item"
        }
    }

    class SampleViewHolderThree(view: View) : ViewHolder<Pair<String, String>>(view) {

        val textOne by bind<TextView>(R.id.item_sample_three_text_one)
        val textTwo by bind<TextView>(R.id.item_sample_three_text_two)

        override fun bind(item: Pair<String, String>) {
            itemView.onClick { onClick?.invoke(2, item) }

            textOne.text = item.first
            textTwo.text = item.second
        }
    }
}