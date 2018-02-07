package com.androidkotlincore.mvp.addons

import kotlin.coroutines.experimental.suspendCoroutine

/**
 * Created by Peter on 31.01.18.
 */
interface CompositeEventListener<TEvent> {
    fun subscribe(listener: (TEvent) -> Unit): (TEvent) -> Unit
    fun unSubscribe(listener: (TEvent) -> Unit): (TEvent) -> Unit
    fun unSubscribeAll()
}

suspend fun <TEvent> CompositeEventListener<TEvent>.awaitFirst(predicate: (TEvent) -> Boolean = { true }): TEvent {
    return suspendCoroutine { continuation ->
        lateinit var listener: (TEvent) -> Unit
        listener = { event: TEvent ->
            if (predicate(event)) {
                continuation.resume(event)
                unSubscribe(listener)
            }
        }
        subscribe(listener)
    }
}