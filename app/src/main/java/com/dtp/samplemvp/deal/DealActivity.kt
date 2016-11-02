package com.dtp.samplemvp.deal

import android.os.Bundle
import android.support.design.widget.CollapsingToolbarLayout
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.LinearSnapHelper
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SnapHelper
import android.view.View
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

        imageRecycler.onFlingListener = LinearSnapHelper()
        imageRecycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        collapsingToolbar.title = "Today's Deal"

        view = this
        presenter = DealPresenter(this)

        if (savedInstanceState != null)
            presenter.start(PresenterData(savedInstanceState))
        else
            presenter.start(null)
    }

    override fun displayError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override fun displayImages(imageUrls: List<String>) {
    }
}
