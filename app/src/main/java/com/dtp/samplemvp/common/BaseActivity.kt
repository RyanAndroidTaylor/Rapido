package com.dtp.samplemvp.common

import android.support.v7.app.AppCompatActivity
import com.dtp.rapido.mvp.presenter.Presenter
import com.dtp.rapido.mvp.view.ViewLayer

/**
 * Created by ryantaylor on 10/8/16.
 */
open class BaseActivity<V: ViewLayer, P: Presenter<V>> : AppCompatActivity() {

    lateinit var presenter: P
    lateinit var view: V

    override fun onResume() {
        super.onResume()

        presenter.view = view
    }

    override fun onPause() {
        super.onPause()

        presenter.view = null

        if (isFinishing)
            presenter.destroy()
    }
}