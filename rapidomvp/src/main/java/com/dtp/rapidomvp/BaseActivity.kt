package com.dtp.rapidomvp

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.dtp.rapidomvp.presenter.Presenter
import com.dtp.rapidomvp.view.ViewLayer

/**
 * Created by ner on 1/31/17.
 */
abstract class BaseMvpActivity<V: ViewLayer, P: Presenter<V>> : AppCompatActivity() {

    protected abstract val view: V

    lateinit var presenter: P

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        presenter = createPresenter()
    }

    override fun onResume() {
        super.onResume()

        presenter.subscribe(view)
    }

    override fun onPause() {
        super.onPause()

        presenter.unSubscribe()

        if (isFinishing)
            presenter.destroy()
    }

    abstract fun createPresenter(): P
}