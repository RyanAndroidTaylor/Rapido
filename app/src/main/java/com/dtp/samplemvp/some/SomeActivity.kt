package com.dtp.samplemvp.some

import android.os.Bundle
import com.dtp.rapido.mvp.presenter.PresenterData
import com.dtp.samplemvp.common.BaseActivity

/**
 * Created by ner on 11/2/16.
 */
class SomeActivity: BaseActivity<SomeView, SomePresenter>(), SomeView {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        view = this

        presenter = SomePresenter()

        if (savedInstanceState != null)
            presenter.load(PresenterData(savedInstanceState))
        else
            presenter.load(null)
    }
}