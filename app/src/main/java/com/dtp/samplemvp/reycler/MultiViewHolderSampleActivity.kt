package com.dtp.samplemvp.reycler

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.SimpleAdapter
import android.widget.TextView
import com.dtp.samplemvp.R
import com.izeni.rapidocommon.recycler.MultiViewHolderAdapter
import com.izeni.rapidocommon.recycler.MultiViewHolderAdapter.SectionData
import com.izeni.rapidocommon.recycler.MultiViewHolderAdapter.Section
import com.izeni.rapidocommon.recycler.ViewHolder
import com.izeni.rapidocommon.view.bind

/**
 * Created by ner on 1/11/17.
 */
class MultiViewHolderSampleActivity : AppCompatActivity() {

    val recyclerView  by bind<RecyclerView>(R.id.multi_view_holder_recycler)

    var adapter: SampleAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_multi_view_holder_sample)

        val listOne = mutableListOf("One", "Two", "Three", "Four", "Five", "Six")
        val listTwo = mutableListOf(1, 2, 3, 4, 5, 6)
        val listThree = mutableListOf("One" to "1", "Two" to "2", "Three" to "3", "Four" to "4", "Five" to "5", "Six" to "6")

        adapter = SampleAdapter(listOf(SampleSectionOne(listOne), SampleSectionTwo(listTwo), SampleSectionThree(listThree)))

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    class SampleAdapter(sections: List<Section<*>>):
            MultiViewHolderAdapter(sections, ViewHolderData(R.layout.item_sample_section, { SectionViewHolder(it) }))

    class SectionViewHolder(view: View): ViewHolder<SectionData>(view) {
        override fun bind(item: SectionData) {

        }
    }

    class SampleSectionOne(items: MutableList<String>):
            Section<String>(1, items, MultiViewHolderAdapter.ViewHolderData(R.layout.item_sample_one, { SampleViewHolderOne(it) }), true)

    class SampleViewHolderOne(view: View): ViewHolder<String>(view) {

        val text by bind<TextView>(R.id.sample_item_text)

        override fun bind(item: String) {
            text.text = item
        }
    }

    class SampleSectionTwo(items: MutableList<Int>):
            Section<Int>(2, items, MultiViewHolderAdapter.ViewHolderData(R.layout.item_sample_two, { SampleViewHolderTwo(it) }), true)

    class SampleViewHolderTwo(view: View): ViewHolder<Int>(view) {

        val text by bind<TextView>(R.id.sample_item_two_text)

        override fun bind(item: Int) {
            text.text = "$item"
        }
    }

    class SampleSectionThree(items: MutableList<Pair<String, String>>):
            Section<Pair<String, String>>(3, items, MultiViewHolderAdapter.ViewHolderData(R.layout.item_sample_three, { SampleViewHolderThree(it) }), true)

    class SampleViewHolderThree(view: View): ViewHolder<Pair<String, String>>(view) {

        val textOne by bind<TextView>(R.id.item_sample_three_text_one)
        val textTwo by bind<TextView>(R.id.item_sample_three_text_two)

        override fun bind(item: Pair<String, String>) {
            textOne.text = item.first
            textTwo.text = item.second
        }
    }
}