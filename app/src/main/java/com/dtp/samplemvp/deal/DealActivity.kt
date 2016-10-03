package com.dtp.samplemvp.deal

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast

import com.dtp.samplemvp.R
import com.dtp.samplemvp.common.database.Item
import com.dtp.simplemvp.database.DataConnection
import com.dtp.simplemvp.database.query.QueryBuilder
import kotlinx.android.synthetic.main.activity_main.*
import rx.Subscription

class DealActivity : AppCompatActivity(), DealView {

    private lateinit var dealPresenter: DealPresenter

    var subscription: Subscription? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dealPresenter = DealPresenter(this)

//        dealPresenter.load()

        click.setOnClickListener {
//            dealPresenter.setNewText("Correct text")

            val item = Item("some Uuid", 123L, "Condition poor", 123, "This is a photo")

            DataConnection.save<Item>(item)
        }

        toggle.setOnClickListener {
            if (subscription != null) {
                Log.i("DealActivity", "Unsubscribing")
                subscription?.unsubscribe()
                subscription = null
            } else {
                Log.i("DealActivity", "Subscribing")
                subscription = DataConnection.watchTable<Item>(Item.TABLE_NAME).subscribe {
                    Log.i("DealActivity", "Deal was inserted $it")
                }
            }
        }
    }

    override fun displayError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override fun displayNewText(text: String) {
        text_place.text = text
    }
}
