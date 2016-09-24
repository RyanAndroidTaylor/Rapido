package com.dtp.samplemvp.common.database

import android.content.ContentValues
import android.database.Cursor
import com.dtp.samplemvp.common.util.getInt
import com.dtp.samplemvp.common.util.getLong
import com.dtp.samplemvp.common.util.getString
import com.dtp.simplemvp.Column
import com.dtp.simplemvp.Column.Companion.INTEGER
import com.dtp.simplemvp.Column.Companion.TEXT
import com.dtp.simplemvp.Column.Companion.UUID
import com.dtp.simplemvp.DataTable
import com.dtp.simplemvp.ItemBuilder

/**
 * Created by ryantaylor on 9/22/16.
 */
data class Item(
        val uuid: String,
        val itemId: Long,
        val condition: String,
        val price: Int,
        val photo: String): DataTable {

    companion object {
        val NAME = "Item"
        val ITEM_ID = Column("ItemId", INTEGER)
        val CONDITION = Column("Condition", TEXT)
        val PRICE = Column("Price", INTEGER)
        val PHOTO = Column("Photo", TEXT)

        val COLUMNS = arrayOf(ITEM_ID, CONDITION, PRICE, PHOTO)

        val BUILDER = Builder()
    }

    override fun tableName(): String {
        return NAME
    }

    override fun contentValues(): ContentValues {
        val contentValues = ContentValues()

        contentValues.put(UUID.name, uuid)
        contentValues.put(ITEM_ID.name, itemId)
        contentValues.put(CONDITION.name, condition)
        contentValues.put(PRICE.name, price)
        contentValues.put(PHOTO.name, photo)

        return contentValues
    }

    class Builder: ItemBuilder<Item> {
        override fun buildItem(cursor: Cursor): Item {
            return Item(cursor.getString(UUID.name), cursor.getLong(ITEM_ID.name), cursor.getString(CONDITION.name), cursor.getInt(PRICE.name), cursor.getString(PHOTO.name))
        }
    }
}