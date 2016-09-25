package com.dtp.samplemvp.deal

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

import com.dtp.samplemvp.R
import com.dtp.samplemvp.common.database.Item
import com.dtp.simplemvp.database.DataConnection
import com.dtp.simplemvp.database.query.QueryBuilder

class DealActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val itemOne = Item("uuidOne", 1, "new", 100, "photoOne")
        val itemTwo = Item("uuidTwo", 2, "minimal wear", 75, "photoTwo")
        val itemThree = Item("uuidThree", 3, "old", 50, "photoThree")

        DataConnection.saveAll(listOf(itemOne, itemTwo, itemThree))

        val query = QueryBuilder().select(Item.COLUMNS).from(Item.TABLE_NAME).descending(Item.ITEM_ID).build()

        val loadedItems = DataConnection.findAll(Item.BUILDER, query)

        for (item in loadedItems)
            Log.i("DealActivity", item.toString())
    }
}
