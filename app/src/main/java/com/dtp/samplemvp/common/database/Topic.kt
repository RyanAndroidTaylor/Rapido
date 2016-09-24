package com.dtp.samplemvp.common.database

import android.content.ContentValues
import android.database.Cursor
import com.dtp.simplemvp.database.table.ChildDataTable
import com.dtp.simplemvp.database.item_builder.ChildItemBuilder
import com.dtp.simplemvp.database.table.Column
import com.dtp.simplemvp.database.table.Column.Companion.STRING
import com.dtp.simplemvp.database.table.Column.Companion.INT
import com.dtp.simplemvp.get
import com.dtp.simplemvp.put

/**
 * Created by ryantaylor on 9/23/16.
 */
data class Topic(val id: String, val commentCount: Int, val createdAt: String, val replyCount: Int, val url: String, val voteCount: Int) : ChildDataTable {

    companion object {
        val TABLE_NAME = "Topic"

        val TOPIC_ID = Column(STRING, "TopicId")
        val COMMENT_COUNT = Column(INT, "CommentCount")
        val CREATED_AT = Column(STRING, "CreatedAt")
        val REPLY_COUNT = Column(INT, "ReplyCount")
        val URL = Column(STRING, "Url")
        val VOTE_COUNT = Column(INT, "VoteCount")

        val COLUMNS = arrayOf(TOPIC_ID, COMMENT_COUNT, CREATED_AT, REPLY_COUNT, URL, VOTE_COUNT)

        val BUILDER = Builder()
    }

    override fun tableName(): String {
        return TABLE_NAME
    }

    override fun contentValues(): ContentValues {
        val contentValues = contentValues()

        contentValues.put(TOPIC_ID, id)
        contentValues.put(COMMENT_COUNT, commentCount)
        contentValues.put(CREATED_AT, createdAt)
        contentValues.put(REPLY_COUNT, replyCount)
        contentValues.put(URL, url)
        contentValues.put(VOTE_COUNT, voteCount)

        return contentValues
    }

    class Builder : ChildItemBuilder {
        override fun buildItem(cursor: Cursor): Topic {
            return Topic(cursor.get(TOPIC_ID), cursor.get(COMMENT_COUNT), cursor.get(CREATED_AT), cursor.get(REPLY_COUNT), cursor.get(URL), cursor.get(VOTE_COUNT))
        }

    }
}