@file:Suppress("unused")

package com.izeni.rapidocommon

import com.izeni.rapidocommon.transaction.*
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.*
import kotlin.reflect.KProperty
import android.support.v4.app.Fragment as SupportFragment


/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 Izeni, Inc.
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

fun <T> Observable<T>.onMain(subScheduler: Scheduler? = null): Observable<T> =
        (if (subScheduler != null) this.subscribeOn(subScheduler) else this).observeOn(AndroidSchedulers.mainThread())

fun <T> Observable<T>.watchOnMain(watcher: (T) -> Unit): Disposable =
        onMain().doOnNext(watcher).onErrorResumeNext({ t: Throwable -> Observable.empty<T>() }).subscribe()

fun runOnIo(block: () -> Unit): Disposable {
    return Observable.just(Unit)
            .observeOn(Schedulers.io())
            .subscribe({ block() })
}

fun <T> Observable<T>.filterNetworkErrors(): Observable<Transaction<T>> {
    return lift(ObservableFilterTransactionError())
}

fun <T> Single<T>.filterNetworkErrors(): Single<Transaction<T>> {
    return lift(SingleFilterError())
}

fun String?.prepend(prepend: String) = if (isNullOrEmpty()) "" else "$prepend$this"
fun String?.append(append: String) = if (isNullOrEmpty()) "" else "$this$append"

fun String?.isNothing(): Boolean {
    val strLen: Int? = this?.length
    if (this == null || strLen == 0) return true
    return (0..strLen!!.minus(1)).none { Character.isWhitespace(this[it]) == false }
}

fun String.unescape(): String = this.replace("""\/""", "/")


fun String?.nullSafe(default: String = ""): String = if (this == null) default else this

inline fun <T> T?.ifNotNull(block: (T) -> Unit): T? {
    if (this != null)
        block(this)

    return this
}

inline fun whenNull(block: () -> Unit) {
    block()
}

inline fun <T> T?.ifNull(block: () -> Unit) {
    if (this == null)
        block()
}

inline infix fun Any?.isNotNull(if_true: (Any) -> Unit) {
    if (this != null) if_true.invoke(this)
}

class ResettableLazyManager {
    val managedDelegates = LinkedList<Resettable>()
    private var preReset: ((LinkedList<Resettable>) -> Boolean)? = null

    fun addPreResetListener(preReset: ((LinkedList<Resettable>) -> Boolean)?): ResettableLazyManager {
        this.preReset = preReset
        return this
    }

    fun register(managed: Resettable) {
        synchronized(managedDelegates) { managedDelegates.add(managed) }
    }

    fun reset() {
        synchronized(managedDelegates) {
            preReset?.invoke(managedDelegates)
            managedDelegates.forEach { it.reset() }
            managedDelegates.clear()
        }
    }
}

interface Resettable {
    fun reset()
}

class ResettableLazy<T>(val manager: ResettableLazyManager, val init: () -> T) : Resettable {
    @Volatile var lazyHolder = makeInitBlock()

    operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return lazyHolder.value
    }

    override fun reset() {
        lazyHolder = makeInitBlock()
    }

    fun makeInitBlock(): Lazy<T> {
        return lazy {
            manager.register(this)
            init()
        }
    }
}

fun <T> resettableLazy(manager: ResettableLazyManager, init: () -> T): ResettableLazy<T> {
    return ResettableLazy(manager, init)
}