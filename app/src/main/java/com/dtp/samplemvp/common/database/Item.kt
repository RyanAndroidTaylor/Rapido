package com.dtp.samplemvp.common.database

import android.content.ContentValues
import android.database.Cursor
import com.dtp.rapido.addAll
import com.dtp.rapido.database.table.Column.Companion.UUID
import com.dtp.rapido.database.table.Column.Companion.INT
import com.dtp.rapido.database.table.Column.Companion.LONG
import com.dtp.rapido.database.table.Column.Companion.STRING
import com.dtp.rapido.database.item_builder.ChildItemBuilder
import com.dtp.rapido.database.table.ChildDataTable
import com.dtp.rapido.database.table.Column
import com.dtp.rapido.get

/**
 * Created by ryantaylor on 9/22/16.
 */
data class Item(val id: String, var dealId: String, val condition: String, val price: Int, val photo: String): ChildDataTable {

    companion object {
        val TABLE_NAME = "Item"

        val ITEM_ID = Column(STRING, "ItemId", notNull = true, unique = true)
        val DEAL_ID = Column(STRING, "DealId", foreignKey = Deal.TABLE_NAME to Deal.DEAL_ID, notNull = true)
        val CONDITION = Column(STRING, "Condition")
        val PRICE = Column(INT, "Price")
        val PHOTO = Column(STRING, "Photo")

        val COLUMNS = arrayOf(ITEM_ID, DEAL_ID, CONDITION, PRICE, PHOTO)

        val BUILDER = Builder()
    }

    override fun tableName(): String {
        return TABLE_NAME
    }

    override fun contentValues(): ContentValues {
        return ContentValues().addAll(COLUMNS, arrayOf(id, dealId, condition, price, photo))
    }

    override fun setForeignKeyValue(parentId: String) {
        dealId = parentId
    }

    class Builder: ChildItemBuilder<Item> {
        override val tableName = TABLE_NAME
        override val foreignKey = DEAL_ID

        override fun buildItem(cursor: Cursor): Item {
            return Item(cursor.get(ITEM_ID), cursor.get(DEAL_ID), cursor.get(CONDITION), cursor.get(PRICE), cursor.get(PHOTO))
        }
    }
}