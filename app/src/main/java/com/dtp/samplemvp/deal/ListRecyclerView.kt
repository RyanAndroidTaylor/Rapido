package com.dtp.samplemvp.deal

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.dtp.samplemvp.R
import com.dtp.samplemvp.common.database.Photo
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.deal_image_view.view.*

/**
 * Created by ryantaylor on 10/9/16.
 */
class ListRecyclerView(var items: List<String>) : RecyclerView.Adapter<ListRecyclerView.ViewHolder>() {

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder?.onBind(items[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.deal_image_view, parent, false)

        return ViewHolder(view)
    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {

        var imageView: ImageView

        init {
            imageView = view.deal_image
        }

        fun onBind(photo: String) {
            Picasso.with(itemView.context).load(photo).fit().into(imageView)
        }
    }
}