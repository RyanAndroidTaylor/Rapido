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
        var ifNotNullBlockWasRun = false
        var whenNullBlockWasRun = false

        val testVariable: String? = null

        testVariable?.ifNotNull {
            ifNotNullBlockWasRun = true
        } ?: whenNull {
            whenNullBlockWasRun = true
        }

        assertFalse(ifNotNullBlockWasRun)
        assertTrue(whenNullBlockWasRun)
    }

    @Test
    fun testNotNull() {
        var ifNotNullBlockWasRun = false
        var whenNullBlockWasRun = false

        val testVariable: String? = "Something"

        testVariable?.ifNotNull {
            ifNotNullBlockWasRun = true
        } ?: whenNull {
            whenNullBlockWasRun = true
        }

        assertFalse(whenNullBlockWasRun)
        assertTrue(ifNotNullBlockWasRun)
    }

    @Test
    fun testNotNullEndWithNoReturnMethod() {
        var ifNotNullBlockWasRun = false
        var whenNullBlockWasRun = false

        val testVariable: String? = "Something"

        testVariable?.ifNotNull {
            ifNotNullBlockWasRun = true

            noReturnMethod()
        } ?: whenNull {
            whenNullBlockWasRun = true
        }

        assertFalse(whenNullBlockWasRun)
        assertTrue(ifNotNullBlockWasRun)
    }

    @Test
    fun testNotNullEndWithReturnNotNullMethod() {
        var ifNotNullBlockWasRun = false
        var whenNullBlockWasRun = false

        val testVariable: String? = "Something"

        testVariable?.ifNotNull {
            ifNotNullBlockWasRun = true

            returnNotNullMethod()
        } ?: whenNull {
            whenNullBlockWasRun = true
        }

        assertFalse(whenNullBlockWasRun)
        assertTrue(ifNotNullBlockWasRun)
    }

    @Test
    fun testNotNullEndWithReturnNullMethod() {
        var ifNotNullBlockWasRun = false
        var whenNullBlockWasRun = false

        val testVariable: String? = "Something"

        testVariable?.ifNotNull {
            ifNotNullBlockWasRun = true

            returnNullMethod()
        } ?: whenNull {
            whenNullBlockWasRun = true
        }

        assertFalse(whenNullBlockWasRun)
        assertTrue(ifNotNullBlockWasRun)
    }

    @Test
    fun endsWithNull() {
        var ifNotNullBlockWasRun = false
        var whenNullBlockWasRun = false

        val testVariable: String? = "Something"

        testVariable?.ifNotNull {
            ifNotNullBlockWasRun = true

            null
        } ?: whenNull {
            whenNullBlockWasRun = true
        }

        assertFalse(whenNullBlockWasRun)
        assertTrue(ifNotNullBlockWasRun)
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