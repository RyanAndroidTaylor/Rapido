package com.dtp.rapidomvp

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.dtp.rapidomvp.presenter.Presenter
import com.dtp.rapidomvp.view.ViewLayer

/**
 * Created by ner on 1/31/17.
 */
abstract class BaseActivity<V: ViewLayer, P: Presenter<V>> : AppCompatActivity() {

    protected abstract val layoutID: Int
    protected abstract val view: V

    lateinit var presenter: P

    private var isSubscribed = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutID)

        presenter = createPresenter()

        subscribe()
    }

    override fun onResume() {
        super.onResume()

        subscribe()
    }

    override fun onPause() {
        super.onPause()

        unSubscribe()

        if (isFinishing)
            presenter.destroy()
    }

    abstract fun createPresenter(): P

    private fun subscribe() {
        if (!isSubscribed) {
            presenter.subscribe(view)
            isSubscribed = true
        }
    }

    private fun unSubscribe() {
        if (isSubscribed) {
            presenter.unSubscribe()
            isSubscribed = false
        }
    }
}