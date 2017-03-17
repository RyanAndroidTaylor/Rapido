package com.studiesweekly.readings.network

import com.izeni.rapidocommon.transaction.TransactionSubject

/**
 * studies-weekly-android
 * Date: 3/14/17
 */

typealias BasicTransactionSubject<T> = TransactionSubject<T, Unit>
typealias TransactionUnitSubject = BasicTransactionSubject<Unit>