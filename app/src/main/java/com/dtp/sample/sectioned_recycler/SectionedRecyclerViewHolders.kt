package com.dtp.sample.sectioned_recycler

import android.view.View
import android.widget.TextView
import com.dtp.sample.R
import com.izeni.rapidocommon.recycler.SectionedViewHolder
import com.izeni.rapidocommon.view.bind
import com.izeni.rapidocommon.view.onClick

/**
 * Created by ner on 1/31/17.
 */
class SectionOneViewHolder(view: View) : SectionedViewHolder<String>(view) {

    val text by bind<TextView>(R.id.sample_item_text)

    override fun bind(item: String) {
        itemView.onClick { onClick?.invoke(0, item) }

        text.text = item
    }
}

class SectionTwoViewHolder(view: View) : SectionedViewHolder<Int>(view) {

    val text by bind<TextView>(R.id.sample_item_two_text)

    override fun bind(item: Int) {
        itemView.onClick { onClick?.invoke(1, item) }

        text.text = "$item"
    }
}

class SectionThreeViewHolder(view: View) : SectionedViewHolder<Pair<String, String>>(view) {

    val textOne by bind<TextView>(R.id.item_sample_three_text_one)
    val textTwo by bind<TextView>(R.id.item_sample_three_text_two)

    override fun bind(item: Pair<String, String>) {
        itemView.onClick { onClick?.invoke(2, item) }

        textOne.text = item.first
        textTwo.text = item.second
    }
}