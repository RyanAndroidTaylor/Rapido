package com.dtp.samplemvp.common.database

import android.content.ContentValues
import android.database.Cursor
import com.dtp.rapido.addAll
import com.dtp.rapido.database.table.ChildDataTable
import com.dtp.rapido.database.item_builder.ChildItemBuilder
import com.dtp.rapido.database.table.Column
import com.dtp.rapido.database.table.Column.Companion.STRING

import com.dtp.rapido.get

/**
 * Created by ryantaylor on 9/23/16.
 */
data class Theme(val dealId: String, val accentColor: String, val backgroundColor: String, val backgroundImage: String, val foreground: String): ChildDataTable {

    companion object {
        val TABLE_NAME = "Theme"

        val DEAL_ID = Column(STRING, "DealId", foreignKey = Deal.TABLE_NAME to Deal.DEAL_ID, notNull = true, unique = true)
        val ACCENT_COLOR = Column(STRING, "AccentColor")
        val BACKGROUND_COLOR = Column(STRING, "BackgroundColor")
        val BACKGROUND_IMAGE = Column(STRING, "BackgroundImage")
        val FOREGROUND = Column(STRING, "Foreground")

        val COLUMNS = arrayOf(DEAL_ID, ACCENT_COLOR, BACKGROUND_COLOR, BACKGROUND_IMAGE, FOREGROUND)

        val BUILDER = Builder()
    }

    override fun tableName(): String {
        return TABLE_NAME
    }

    override fun contentValues(): ContentValues {
        return ContentValues().addAll(COLUMNS, arrayOf(dealId, accentColor, backgroundColor, backgroundImage, foreground))
    }

    class Builder: ChildItemBuilder<Theme> {
        override val tableName = TABLE_NAME
        override val foreignKey = DEAL_ID

        override fun buildItem(cursor: Cursor): Theme {
            return Theme(cursor.get(DEAL_ID), cursor.get(ACCENT_COLOR), cursor.get(BACKGROUND_COLOR), cursor.get(BACKGROUND_IMAGE), cursor.get(FOREGROUND))
        }
    }
}