package com.dtp.samplemvp.common.database

import android.content.ContentValues
import android.database.Cursor
import com.dtp.rapido.addAll
import com.dtp.rapido.database.table.ChildDataTable
import com.dtp.rapido.database.item_builder.ChildItemBuilder
import com.dtp.rapido.database.table.Column
import com.dtp.rapido.database.table.Column.Companion.STRING
import com.dtp.rapido.database.table.Column.Companion.INT
import com.dtp.rapido.get
import com.dtp.rapido.get

/**
 * Created by ryantaylor on 9/23/16.
 */
data class Topic(val id: String, var dealId: String, val commentCount: Int, val createdAt: String, val replyCount: Int, val url: String, val voteCount: Int) : ChildDataTable {

    companion object {
        val TABLE_NAME = "Topic"

        val TOPIC_ID = Column(STRING, "TopicId", notNull = true, unique = true)
        val DEAL_ID = Column(STRING, "DealId", foreignKey = Deal.TABLE_NAME to Deal.DEAL_ID, notNull = true)
        val COMMENT_COUNT = Column(INT, "CommentCount")
        val CREATED_AT = Column(STRING, "CreatedAt")
        val REPLY_COUNT = Column(INT, "ReplyCount")
        val URL = Column(STRING, "Url")
        val VOTE_COUNT = Column(INT, "VoteCount")

        val COLUMNS = arrayOf(TOPIC_ID, DEAL_ID, COMMENT_COUNT, CREATED_AT, REPLY_COUNT, URL, VOTE_COUNT)

        val BUILDER = Builder()
    }

    override fun tableName(): String {
        return TABLE_NAME
    }

    override fun contentValues(): ContentValues {
        return ContentValues().addAll(COLUMNS, arrayOf(id, dealId, commentCount, createdAt, replyCount, url, voteCount))
    }

    class Builder : ChildItemBuilder<Topic> {
        override val tableName = TABLE_NAME
        override val foreignKey = DEAL_ID

        override fun buildItem(cursor: Cursor): Topic {
            return Topic(cursor.get(TOPIC_ID), cursor.get(DEAL_ID), cursor.get(COMMENT_COUNT), cursor.get(CREATED_AT), cursor.get(REPLY_COUNT), cursor.get(URL), cursor.get(VOTE_COUNT))
        }

    }
}