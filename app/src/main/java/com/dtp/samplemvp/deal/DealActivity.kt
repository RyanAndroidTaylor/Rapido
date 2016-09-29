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

class DealActivity : AppCompatActivity(), DealView {

    private lateinit var dealPresenter: DealPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dealPresenter = DealPresenter(this)

        dealPresenter.load()

        click.setOnClickListener { dealPresenter.setNewText("Correct text") }
    }

    override fun displayError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override fun displayNewText(text: String) {
        text_place.text = text
    }
}
