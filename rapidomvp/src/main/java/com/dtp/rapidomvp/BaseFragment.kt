package com.dtp.rapidomvp

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dtp.rapidomvp.presenter.Presenter
import com.dtp.rapidomvp.view.ViewLayer

/**
 * Created by ner on 3/13/17.
 */
abstract class BaseFragment<V: ViewLayer, P: Presenter<V>> : Fragment() {
    abstract val fragmentView: Int

    protected abstract val view: V

    lateinit var presenter: P

    private var isSubscribed = false

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val content = inflater?.inflate(fragmentView, container, false)

        createPresenter()

        return content
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subscribe()
    }

    abstract fun createPresenter(): P

    override fun onPause() {
        super.onPause()

        unSubscribe()
    }

    override fun onDestroy() {
        super.onDestroy()

        presenter.destroy()
    }

    override fun onResume() {
        super.onResume()

        subscribe()
    }

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