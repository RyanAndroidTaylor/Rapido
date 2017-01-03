@file:Suppress("unused")

package com.izeni.rapidocommon

import android.support.annotation.IntDef
import android.util.Log

/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 Camaron Crowe.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 **/
private val MAX_LOG_LENGTH = 4000

object kLog {

    const val WTF: Int = 8
    const val ASSERT: Int = 7
    const val ERROR: Int = 6
    const val WARN: Int = 5
    const val INFO: Int = 4
    const val DEBUG: Int = 3
    const val VERBOSE: Int = 2
    const val ALL: Int = 1

    @LoggingLevel var loggingLevel: Int = ALL

    var defaultTag = "kLog"
    var postPrintLogic: (priority: Int, tag: String, msg: String) -> Boolean = { p, t, m -> true }

    @IntDef(*longArrayOf(8, 7, 6, 5, 4, 3, 2, 1))
    @Retention(AnnotationRetention.SOURCE)
    annotation class LoggingLevel

    var enableLineWrapping: Boolean = true
    var enableStackLog: Boolean = true

    @Suppress("NOTHING_TO_INLINE")
    inline fun stack_trace(message: String?, stack_depth: Int = 4): String {
        val stackTrace = Thread.currentThread().stackTrace

        val fullClassName = stackTrace[stack_depth].fileName
        val methodName = stackTrace[stack_depth].methodName
        val lineNumber = stackTrace[stack_depth].lineNumber

        val shortMN = methodName.substring(0, 1).toUpperCase() + methodName.substring(1)

        return "($fullClassName:$lineNumber) $shortMN() - $message"
    }

    private fun logMessage(priority: Int, tag: String, message: String) {
        if (message.length < MAX_LOG_LENGTH || !enableLineWrapping) {
            println(priority, tag, message)
            return
        }

        // Split by line, then ensure each line can fit into Log's maximum length.
        var i = 0
        val length = message.length
        while (i < length) {
            var newline = message.indexOf('\n', i)
            newline = if (newline != -1) newline else length
            do {
                val end = Math.min(newline, i + MAX_LOG_LENGTH)
                println(priority, tag, message.substring(i, end))
                i = end
            } while (i < newline)
            i++
        }
    }

    fun println(priority: Int, tag: String, msg: String): Int {
        if (!postPrintLogic(priority, tag, msg)) return 0
        return Log.println(priority, tag, msg)
    }

    fun log(message: String?, tag: String? = null, t: Throwable? = null, @LoggingLevel priority: Int = DEBUG, log_line: Boolean = enableStackLog) {
        if (priority >= loggingLevel) {
            var msg = if (log_line) stack_trace(message) else message

            if (msg == null || msg.isEmpty()) {
                if (t == null) return
                msg = Log.getStackTraceString(t)
            } else {
                if (t != null) msg += "\n" + Log.getStackTraceString(t)
            }

            logMessage(priority, tag ?: defaultTag, msg!!)
        }
    }
}

fun v(message: String?, tag: String? = null, t: Throwable? = null, log_line: Boolean = kLog.enableStackLog) {
    kLog.log(if (log_line) kLog.stack_trace(message) else message, tag, t, log_line = false, priority = kLog.VERBOSE)
}

fun d(message: String?, tag: String? = null, t: Throwable? = null, log_line: Boolean = kLog.enableStackLog) {
    kLog.log(if (log_line) kLog.stack_trace(message) else message, tag, t, log_line = false, priority = kLog.DEBUG)
}

fun i(message: String?, tag: String? = null, t: Throwable? = null, log_line: Boolean = kLog.enableStackLog) {
    kLog.log(if (log_line) kLog.stack_trace(message) else message, tag, t, log_line = false, priority = kLog.INFO)
}

fun w(message: String?, tag: String? = null, t: Throwable? = null, log_line: Boolean = kLog.enableStackLog) {
    kLog.log(if (log_line) kLog.stack_trace(message) else message, tag, t, log_line = false, priority = kLog.WARN)
}

fun e(message: String?, tag: String? = null, t: Throwable? = null, log_line: Boolean = kLog.enableStackLog) {
    kLog.log(if (log_line) kLog.stack_trace(message) else message, tag, t, log_line = false, priority = kLog.ERROR)
}

fun a(message: String?, tag: String? = null, t: Throwable? = null, log_line: Boolean = kLog.enableStackLog) {
    kLog.log(if (log_line) kLog.stack_trace(message) else message, tag, t, log_line = false, priority = kLog.ASSERT)
}

fun wtf(message: String?, tag: String? = null, t: Throwable? = null, log_line: Boolean = kLog.enableStackLog) {
    kLog.log(if (log_line) kLog.stack_trace(message) else message, tag, t, log_line = false, priority = kLog.WTF)
}