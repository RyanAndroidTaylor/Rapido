package com.izeni.rapidocommon

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).

 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
class WhenNullTests {

    @Test
    @Throws(Exception::class)
    fun testNull() {
        var letBlockWasRun = false
        var whenNullBlockWasRun = false

        val testVariable: String? = null

        testVariable?.let {
            letBlockWasRun = true
        } ?: whenNull {
            whenNullBlockWasRun = true
        }

        assertFalse(letBlockWasRun)
        assertTrue(whenNullBlockWasRun)
    }

    @Test
    fun testNotNull() {
        var letBlockWasRun = false
        var whenNullBlockWasRun = false

        val testVariable: String? = "Something"

        testVariable?.let {
            letBlockWasRun = true
        } ?: whenNull {
            whenNullBlockWasRun = true
        }

        assertFalse(whenNullBlockWasRun)
        assertTrue(letBlockWasRun)
    }

    @Test
    fun testNotNullEndWithNoReturnMethod() {
        var letBlockWasRun = false
        var whenNullBlockWasRun = false

        val testVariable: String? = "Something"

        testVariable?.let {
            letBlockWasRun = true

            noReturnMethod()
        } ?: whenNull {
            whenNullBlockWasRun = true
        }

        assertFalse(whenNullBlockWasRun)
        assertTrue(letBlockWasRun)
    }

    @Test
    fun testNotNullEndWithReturnNotNullMethod() {
        var letBlockWasRun = false
        var whenNullBlockWasRun = false

        val testVariable: String? = "Something"

        testVariable?.let {
            letBlockWasRun = true

            returnNotNullMethod()
        } ?: whenNull {
            whenNullBlockWasRun = true
        }

        assertFalse(whenNullBlockWasRun)
        assertTrue(letBlockWasRun)
    }

    @Test
    fun testNotNullEndWithReturnNullMethod() {
        var letBlockWasRun = false
        var whenNullBlockWasRun = false

        val testVariable: String? = "Something"

        testVariable?.let {
            letBlockWasRun = true

            returnNullMethod()
        } ?: whenNull {
            whenNullBlockWasRun = true
        }

        assertTrue(whenNullBlockWasRun)
        assertTrue(letBlockWasRun)
    }

    private fun noReturnMethod() {

    }

    private fun returnNullMethod(): String? {
        return null
    }

    private fun returnNotNullMethod(): String? {
        return "Something"
    }
}