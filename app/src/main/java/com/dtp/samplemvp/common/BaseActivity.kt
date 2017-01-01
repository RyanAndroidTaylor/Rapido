package com.dtp.samplemvp.common

import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import com.dtp.rapidomvp.presenter.Presenter
import com.dtp.rapidomvp.view.ViewLayer

/**
 * Created by ryantaylor on 10/8/16.
 */
open class BaseActivity<V: ViewLayer, P: Presenter<V>> : AppCompatActivity() {

    lateinit var presenter: P
    lateinit var view: V

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

    override fun onSaveInstanceState(outState: Bundle?, outPersistentState: PersistableBundle?) {

        super.onSaveInstanceState(outState, outPersistentState)
    }
}