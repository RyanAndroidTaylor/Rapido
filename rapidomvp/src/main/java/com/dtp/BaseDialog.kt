package com.dtp

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import com.dtp.rapidomvp.presenter.Presenter
import com.dtp.rapidomvp.view.ViewLayer

/**
 * Created by ner on 5/8/17.
 */
abstract class BaseDialog<V : ViewLayer, P : Presenter<V>>(context: Context) : Dialog(context) {
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

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        subscribe()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()

        unSubscribe()

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