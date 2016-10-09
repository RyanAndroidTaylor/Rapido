package com.dtp.samplemvp.deal

import android.os.Bundle
import android.widget.Toast
import com.dtp.rapido.mvp.presenter.PresenterData

import com.dtp.samplemvp.R
import com.dtp.samplemvp.common.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*

class DealActivity : BaseActivity<DealView, DealPresenter>(), DealView {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        view = this
        presenter = DealPresenter(this)

        if (savedInstanceState != null)
            presenter.load(PresenterData(savedInstanceState))
        else
            presenter.load(null)
    }

    override fun displayError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override fun displayNewText(text: String) {
        text_place.text = text
    }
}
