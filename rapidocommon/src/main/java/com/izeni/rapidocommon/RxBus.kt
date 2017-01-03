package com.izeni.rapidocommon

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject

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
abstract class AbstractRxBus(val bus_key: String) {
    val _bus: PublishSubject<Any> = PublishSubject.create<Any>()

    fun subscribe(subscription: CompositeDisposable, on_next: (Any) -> Unit) {
        if(Rapido.rxBusLogging) {
            val STACK_DEPTH: Int = 3
            val stackTrace = Thread.currentThread().stackTrace
            val fullClassName = stackTrace[STACK_DEPTH].fileName
            val methodName = stackTrace[STACK_DEPTH].methodName
            val shortMN = methodName.substring(0, 1).toUpperCase() + methodName.substring(1)
            val lineNumber = stackTrace[STACK_DEPTH].lineNumber

            i("($fullClassName:$lineNumber) $shortMN() - $fullClassName subscribed to $bus_key", log_line = false)
        }
        subscription.add(_bus.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(AndroidSchedulers.mainThread()).subscribe(on_next))
    }

    fun send(o: Any) {
        if(Rapido.rxBusLogging) i("Event sent from $bus_key: ${o.javaClass.canonicalName.split('.').last()}")

        _bus.onNext(o)
    }
    fun toObservable(): Observable<Any> = _bus
    fun hasObservers() = _bus.hasObservers()
}

object RxBus: AbstractRxBus("RxBus")