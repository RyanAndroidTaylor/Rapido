package com.izeni.rapidocommon

import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Created by ner on 1/2/17.
 */
fun ViewGroup.inflate(@LayoutRes resourceId: Int, attach: Boolean = false): View {
    return LayoutInflater.from(context).inflate(resourceId, this, attach)
}