package com.dtp.samplemvp

import com.izeni.rapidocommon.d
import com.izeni.rapidocommon.whenNull

/**
 * Created by ner on 2/1/17.
 */
class TestWhenNull {

    var testOne: String? = null

    fun test() {
        testOne?.let {
            d("testOne is not null")
        } ?: whenNull {
            d("testOne is null and ?: whenNull works")
        }

        testOne = "Set to not null"

        testOne?.let {
            d(it)
        } ?: whenNull {
            d("testOne is not null so this should never happen")
        }

        testOne?.let {
            d("Calling void method")

            voidReturn()
        } ?: whenNull {
            d("testOne is not null. Ending let with void method")
        }

        var otherVariable: String? = ""
        testOne?.let {
            d(it)

            otherVariable = null
        } ?: whenNull {
            d("testOne is not null. Setting a variable to null at the end of let{}")
        }

        testOne?.let {
            d(it)

            null
        } ?: whenNull {
            d("testOne is not null. Ending let with null value")
        }
    }

    fun voidReturn() {

    }
}