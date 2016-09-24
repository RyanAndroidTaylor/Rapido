package com.dtp.simplemvp.mvp

import android.os.Bundle
import android.os.Parcelable

/**
 * Created by ryantaylor on 9/24/16.
 */
interface State : Parcelable {
    val tag: String

    fun save(saveInstanceState: Bundle) {
        saveInstanceState.putParcelable(tag, this)
    }

    fun load(savedInstanceState: Bundle): State
}