package com.dtp.samplemvp.common.database

import android.content.ContentValues
import android.database.Cursor
import com.dtp.simplemvp.database.*
import com.dtp.simplemvp.database.table.Column.Companion.STRING
import com.dtp.simplemvp.database.item_builder.ChildItemBuilder
import com.dtp.simplemvp.database.item_builder.ParentItemBuilder
import com.dtp.simplemvp.database.table.ChildDataTable
import com.dtp.simplemvp.database.table.Column
import com.dtp.simplemvp.database.table.ParentDataTable
import com.dtp.simplemvp.get
import com.dtp.simplemvp.put
import java.util.*

/**
 * Created by ryantaylor on 9/22/16.
 */
class Deal(
        val features: String,
        val id: String,
        val items: List<Item>,
        val photos: List<String>,
        val title: String,
        val story: String,
        val theme: Theme,
        val url: String,
        val topic: Topic?
): ParentDataTable {

    companion object {
        val TABLE_NAME = "Deal"

        val FEATURES = Column(STRING, "Features")
        val DEAL_ID = Column(STRING, "DealId")
        val TITLE = Column(STRING, "Title")
        val STORY = Column(STRING, "Story")
        val URL = Column(STRING, "Url")

        val COLUMNS = arrayOf(FEATURES, DEAL_ID, TITLE, STORY, URL)

        val BUILDER = Builder()
    }

    override fun tableName(): String {
        return TABLE_NAME
    }

    override fun contentValues(): ContentValues {
        val contentValues = ContentValues()

        contentValues.put(FEATURES, features)
        contentValues.put(DEAL_ID, id)
        contentValues.put(TITLE, title)
        contentValues.put(STORY, story)
        contentValues.put(URL, url)

        return contentValues
    }

    override fun getChildren(): List<ChildDataTable> {
        val children = ArrayList<ChildDataTable>()

        topic?.let { children.add(it) }
        children.addAll(items)

        return children
    }

    class Builder: ParentItemBuilder<Deal>() {

        override fun buildItem(cursor: Cursor, children: List<ChildDataTable>?): Deal {
            val items = ArrayList<Item>()
            var topic: Topic? = null
            var theme: Theme? = null

            children?.let {
                for (child in it) {
                    if (child is Theme)
                        theme = child
                    if (child is Topic)
                        topic = child
                    if (child is Item)
                        items.add(child)
                }
            }

            return Deal(cursor.get(FEATURES), cursor.get(DEAL_ID), items, listOf("Need to figure out photo urls"), cursor.get(TITLE), cursor.get(STORY), theme!!, cursor.get(URL), topic)
        }

        override fun getChildBuilders(): List<Pair<String, ChildItemBuilder>> {
            return listOf(Item.TABLE_NAME to Item.BUILDER, Topic.TABLE_NAME to Topic.BUILDER)
        }
    }
}