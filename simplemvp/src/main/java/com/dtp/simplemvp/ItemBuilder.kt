package com.dtp.simplemvp

import android.database.Cursor

/**
 * Created by ryantaylor on 9/23/16.
 */
interface ItemBuilder<out T> {
    fun buildItem(cursor: Cursor): T
}