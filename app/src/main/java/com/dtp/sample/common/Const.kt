package com.dtp.sample.common

import java.util.*

/**
 * Created by ryantaylor on 9/22/16.
 */
object Const {

    val MEH_API_KEY = "3oHU73JMjriwKP3y4ASxSZNSSEPN0lhx"

    fun createUuid(): String {
        return UUID.randomUUID().toString().replace("-", "")
    }
}