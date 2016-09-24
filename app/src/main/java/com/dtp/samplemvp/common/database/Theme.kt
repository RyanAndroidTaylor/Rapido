package com.dtp.samplemvp.common.database

import android.content.ContentValues
import android.database.Cursor
import com.dtp.simplemvp.database.ChildDataTable
import com.dtp.simplemvp.database.ChildItemBuilder
import com.dtp.simplemvp.database.Column
import com.dtp.simplemvp.database.Column.Companion.STRING

import com.dtp.simplemvp.database.ItemBuilder
import com.dtp.simplemvp.get
import com.dtp.simplemvp.put

/**
 * Created by ryantaylor on 9/23/16.
 */
data class Theme(val accentColor: String, val backgroundColor: String, val backgroundImage: String, val foreground: String): ChildDataTable {

    companion object {
        val TABLE_NAME = "Theme"
        val ACCENT_COLOR = Column(STRING, "AccentColor")
        val BACKGROUND_COLOR = Column(STRING, "BackgroundColor")
        val BACKGROUND_IMAGE = Column(STRING, "BackgroundImage")
        val FOREGROUND = Column(STRING, "Foreground")

        val COLUMNS = arrayOf(ACCENT_COLOR, BACKGROUND_COLOR, BACKGROUND_IMAGE, FOREGROUND)

        val BUILDER = Builder()
    }

    override fun tableName(): String {
        return TABLE_NAME
    }

    override fun contentValues(): ContentValues {
        val contentValues = ContentValues()

        contentValues.put(ACCENT_COLOR, accentColor)
        contentValues.put(BACKGROUND_COLOR, backgroundColor)
        contentValues.put(BACKGROUND_IMAGE, backgroundImage)
        contentValues.put(FOREGROUND, foreground)

        return contentValues
    }

    class Builder: ChildItemBuilder {
        override fun buildItem(cursor: Cursor): Theme {
            return Theme(cursor.get(ACCENT_COLOR), cursor.get(BACKGROUND_COLOR), cursor.get(BACKGROUND_IMAGE), cursor.get(FOREGROUND))
        }
    }
}