package com.dtp.sample.sectioned_recycler

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.dtp.sample.R
import com.dtp.sample.TestWhenNull
import com.izeni.rapidocommon.recycler.MultiTypeSectionedAdapter
import com.izeni.rapidocommon.recycler.MultiTypeSectionedAdapter.Section
import com.izeni.rapidocommon.recycler.MultiTypeSectionedAdapter.SectionedViewHolderData
import com.izeni.rapidocommon.recycler.SectionManager.SectionData
import com.izeni.rapidocommon.recycler.SectionedViewHolder
import com.izeni.rapidocommon.view.bind
import com.izeni.rapidocommon.view.onClick
import kotlinx.android.synthetic.main.activity_multi_view_holder_sample.*
import java.util.*

/**
 * Created by ner on 1/11/17.
 */
class SectionedRecyclerActivity : AppCompatActivity() {

    val recyclerView  by bind<RecyclerView>(R.id.multi_view_holder_recycler)

    var adapter: SectionedAdapter? = null

    val random = Random()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_multi_view_holder_sample)

        TestWhenNull().test()

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
                SectionedViewHolderData(R.layout.item_sample_one, { SectionOneViewHolder(it) }),
                hasHeader = true,
                isCollapsible = true) {}

        val sectionTwo = object : Section<Int>(
                1,
                mutableListOf(1, 2, 3, 4, 5, 6),
                SectionedViewHolderData(R.layout.item_sample_two, { SectionTwoViewHolder(it) }),
                hasHeader = true,
                isCollapsible = true) {}

        val sectionThree = object : Section<Pair<String, String>>(
                2,
                mutableListOf("One" to "1", "Two" to "2", "Three" to "3", "Four" to "4", "Five" to "5", "Six" to "6"),
                SectionedViewHolderData(R.layout.item_sample_three, { SectionThreeViewHolder(it) }),
                hasHeader = true,
                isCollapsible = true) {}

        adapter = SectionedAdapter(listOf(sectionOne, sectionTwo, sectionThree),
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
}