package com.androidkotlincore.mvp_rx1

import com.androidkotlincore.mvp.addons.CompositeEventListener
import rx.Emitter
import rx.Observable

/**
 * Created by Peter on 31.01.18.
 */
fun <TEvent> CompositeEventListener<TEvent>.rx(): Observable<TEvent> {
    return Observable.create<TEvent> ({ emitter ->
        subscribe { event -> emitter.onNext(event) }
    }, Emitter.BackpressureMode.BUFFER)
}