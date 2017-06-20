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
            assertNotNull(testVariable)

            ifNotNullBlockWasRun = true
        } ?: whenNull {
            assertNull(testVariable)

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
            assertNotNull(testVariable)

            ifNotNullBlockWasRun = true
        } ?: whenNull {
            assertNull(testVariable)

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
            assertNotNull(testVariable)
            ifNotNullBlockWasRun = true

            noReturnMethod()
        } ?: whenNull {
            assertNull(testVariable)

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
            assertNotNull(testVariable)

            ifNotNullBlockWasRun = true

            returnNotNullMethod()
        } ?: whenNull {
            assertNull(testVariable)

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
            assertNotNull(testVariable)

            ifNotNullBlockWasRun = true

            returnNullMethod()
        } ?: whenNull {
            assertNull(testVariable)

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
            assertNotNull(testVariable)

            ifNotNullBlockWasRun = true

            null
        } ?: whenNull {
            assertNull(testVariable)

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