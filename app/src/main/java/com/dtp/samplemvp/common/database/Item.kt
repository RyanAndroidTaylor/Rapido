package com.dtp.samplemvp.common.database

import android.content.ContentValues
import android.database.Cursor
import com.dtp.simplemvp.database.*
import com.dtp.simplemvp.database.Column.Companion.UUID
import com.dtp.simplemvp.database.Column.Companion.ID
import com.dtp.simplemvp.database.Column.Companion.INT
import com.dtp.simplemvp.database.Column.Companion.LONG
import com.dtp.simplemvp.database.Column.Companion.STRING
import com.dtp.simplemvp.get
import com.dtp.simplemvp.put

/**
 * Created by ryantaylor on 9/22/16.
 */
data class Item(
        val uuid: String,
        val itemId: Long,
        val condition: String,
        val price: Int,
        val photo: String): ChildDataTable {

    companion object {
        val TABLE_NAME = "Item"
        val ITEM_ID = Column(LONG, "ItemId")
        val CONDITION = Column(STRING, "Condition")
        val PRICE = Column(INT, "Price")
        val PHOTO = Column(STRING, "Photo")

        val COLUMNS = arrayOf(ID, UUID, ITEM_ID, CONDITION, PRICE, PHOTO)

        val BUILDER = Builder()
    }

    override fun tableName(): String {
        return TABLE_NAME
    }

    override fun contentValues(): ContentValues {
        val contentValues = ContentValues()

        contentValues.put(UUID, uuid)
        contentValues.put(ITEM_ID, itemId)
        contentValues.put(CONDITION, condition)
        contentValues.put(PRICE, price)
        contentValues.put(PHOTO, photo)

        return contentValues
    }

    class Builder: ChildItemBuilder {
        override fun buildItem(cursor: Cursor): Item {
            return Item(cursor.get(UUID), cursor.get(ITEM_ID), cursor.get(CONDITION), cursor.get(PRICE), cursor.get(PHOTO))
        }
    }
}