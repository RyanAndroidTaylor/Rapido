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
 * Created by ryantaylor on 10/8/16.
 */
data class Photo(var dealId: String, val url: String): ChildDataTable {

    companion object {
        val TABLE_NAME = "Photo"

        val DEAL_ID = Column(STRING, "DealId", foreignKey = Pair(Deal.TABLE_NAME, Deal.DEAL_ID), notNull = true)
        val URL = Column(STRING, "Url", unique = true, notNull = true)

        val COLUMNS = arrayOf(DEAL_ID, URL)

        val BUILDER = Builder()
    }

    override fun tableName(): String {
        return TABLE_NAME
    }

    override fun contentValues(): ContentValues {
        return ContentValues().addAll(COLUMNS, arrayOf(dealId, url))
    }

    override fun setForeignKeyValue(foreignKey: String) {
        dealId = foreignKey
    }

    class Builder: ChildItemBuilder<Photo> {
        override val tableName = TABLE_NAME
        override val foreignKey = DEAL_ID

        override fun buildItem(cursor: Cursor): Photo {
            return Photo(cursor.get(DEAL_ID), cursor.get(URL))
        }
    }
}