package com.dtp.samplemvp.deal

import com.dtp.rapido.mvp.view.ViewLayer

/**
 * Created by ryantaylor on 9/22/16.
 */
interface DealView : ViewLayer {

    fun displayError(message: String)

    fun displayImages(imageUrls: List<String>)
}