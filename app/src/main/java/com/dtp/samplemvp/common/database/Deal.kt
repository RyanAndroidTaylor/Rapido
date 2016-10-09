package com.dtp.samplemvp.common.database

import android.content.ContentValues
import android.database.Cursor
import com.dtp.rapido.database.table.Column.Companion.STRING
import com.dtp.rapido.database.item_builder.ChildItemBuilder
import com.dtp.rapido.database.item_builder.ParentItemBuilder
import com.dtp.rapido.database.table.ChildDataTable
import com.dtp.rapido.database.table.Column
import com.dtp.rapido.database.table.ParentDataTable
import com.dtp.rapido.addAll
import com.dtp.rapido.get
import java.util.*

/**
 * Created by ryantaylor on 9/22/16.
 */
class Deal(
        val id: String,
        val features: String,
        val items: List<Item>,
        val photos: List<String>,
        val title: String,
        val story: Story,
        val theme: Theme?,
        val url: String,
        val topic: Topic?
) : ParentDataTable {

    companion object {
        val TABLE_NAME = "Deal"

        val DEAL_ID = Column(STRING, "DealId", notNull = true, unique = true)
        val FEATURES = Column(STRING, "Features")
        val TITLE = Column(STRING, "Title")
        val STORY = Column(STRING, "Story")
        val URL = Column(STRING, "Url")

        val COLUMNS = arrayOf(DEAL_ID, FEATURES, TITLE, URL)

        val BUILDER = Builder()
    }

    override fun tableName(): String {
        return TABLE_NAME
    }

    override fun contentValues(): ContentValues {
        return ContentValues().addAll(COLUMNS, arrayOf(id, features, title, url))
    }

    override fun getChildren(): List<ChildDataTable> {
        val children = ArrayList<ChildDataTable>()

        topic?.let { children.add(it.apply { dealId = id }) }
        children.add(story.apply { dealId = id })

        for (item in items) {
            item.dealId = id

            children.add(item)
        }

        for (photo in photos)
            children.add(Photo(id, photo))

        return children
    }

    class Builder : ParentItemBuilder<Deal>() {
        override val foreignKey: Column = DEAL_ID

        override fun buildItem(cursor: Cursor, children: List<ChildDataTable>?): Deal {
            val items = ArrayList<Item>()
            val photos = ArrayList<String>()
            var topic: Topic? = null
            var theme: Theme? = null
            var story: Story? = null

            children?.let {
                for (child in it) {
                    when (child) {
                        is Theme -> theme = child
                        is Topic -> topic = child
                        is Item -> items.add(child)
                        is Photo -> photos.add(child.url)
                        is Story -> story = child
                    }
                }
            }

            return Deal(cursor.get(FEATURES), cursor.get(DEAL_ID), items, photos, cursor.get(TITLE), story!!, theme, cursor.get(URL), topic)
        }

        override fun getChildBuilders(): List<ChildItemBuilder<*>> {
            return listOf(Item.BUILDER, Topic.BUILDER, Story.BUILDER, Photo.BUILDER, Theme.BUILDER)
        }
    }
}