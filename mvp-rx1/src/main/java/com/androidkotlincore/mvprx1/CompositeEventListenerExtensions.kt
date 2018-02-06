package com.androidkotlincore.mvprx1

import com.androidkotlincore.mvp.addons.CompositeEventListener
import rx.Emitter
import rx.Observable

/**
 * Created by Peter on 31.01.18.
 */
fun <TEvent> CompositeEventListener<TEvent>.rx(): Observable<TEvent> {
    return Observable.create<TEvent>({ emitter ->
        val sub = subscribe { event -> emitter.onNext(event) }
        emitter.setCancellation { unSubscribe(sub) }
    }, Emitter.BackpressureMode.BUFFER)
}