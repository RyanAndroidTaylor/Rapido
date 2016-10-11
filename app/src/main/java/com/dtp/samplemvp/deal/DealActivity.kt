package com.dtp.samplemvp.deal

import android.os.Bundle
import android.support.design.widget.CollapsingToolbarLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.LinearSnapHelper
import android.support.v7.widget.RecyclerView
import android.widget.Toast
import com.dtp.rapido.mvp.presenter.PresenterData

import com.dtp.samplemvp.R
import com.dtp.samplemvp.common.BaseActivity
import kotlinx.android.synthetic.main.activity_deal.*

class DealActivity : BaseActivity<DealView, DealPresenter>(), DealView {

    lateinit var collapsingToolbar: CollapsingToolbarLayout
    lateinit var imageRecycler: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_deal)

        collapsingToolbar = collapsing_deal_toolbar
        imageRecycler = deal_images

        imageRecycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val snapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(imageRecycler)

        collapsingToolbar.title = "Today's Deal"

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

    override fun displayImages(imageUrls: List<String>) {
        imageRecycler.adapter = DealImageAdapter(imageUrls)
    }
}
