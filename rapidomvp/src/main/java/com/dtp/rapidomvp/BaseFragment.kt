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

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val content = inflater?.inflate(fragmentView, container, false)
        return content
    }

    override fun onPause() {
        super.onPause()

        presenter.unSubscribe()
    }

    override fun onDestroy() {
        super.onDestroy()

        presenter.destroy()
    }

    override fun onResume() {
        super.onResume()

        presenter.subscribe(view)
    }
}