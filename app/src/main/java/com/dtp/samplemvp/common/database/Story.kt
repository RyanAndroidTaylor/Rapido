package com.dtp.samplemvp.common.database

import android.content.ContentValues
import android.database.Cursor
import com.dtp.rapido.addAll
import com.dtp.rapido.database.item_builder.ChildItemBuilder
import com.dtp.rapido.database.table.ChildDataTable
import com.dtp.rapido.database.table.Column
import com.dtp.rapido.database.table.Column.Companion.STRING
import com.dtp.rapido.get

/**
 * Created by ryantaylor on 10/9/16.
 */
data class Story(var dealId: String, val title: String, val body: String): ChildDataTable {

    companion object {
        val TABLE_NAME = "Story"

        val DEAL_ID = Column(STRING, "DealId", foreignKey = Deal.TABLE_NAME to Deal.DEAL_ID, notNull = true, unique = true)
        val TITLE = Column(STRING, "Title")
        val BODY = Column(STRING, "Body")

        val COLUMNS = arrayOf(DEAL_ID, TITLE, BODY)

        val BUILDER = Builder()
    }

    override fun tableName(): String {
        return TABLE_NAME
    }

    override fun contentValues(): ContentValues {
        return ContentValues().addAll(COLUMNS, arrayOf(dealId, title, body))
    }

    override fun setForeignKeyValue(foreignKey: String) {
        dealId = foreignKey
    }

    class Builder : ChildItemBuilder<Story> {
        override val tableName = TABLE_NAME
        override val foreignKey = DEAL_ID

        override fun buildItem(cursor: Cursor): Story {
            return Story(cursor.get(DEAL_ID), cursor.get(TITLE), cursor.get(BODY))
        }

    }
}