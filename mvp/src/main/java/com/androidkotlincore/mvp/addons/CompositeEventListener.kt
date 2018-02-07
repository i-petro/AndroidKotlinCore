package com.androidkotlincore.mvp.addons

import kotlin.coroutines.experimental.suspendCoroutine

/**
 * Created by Peter on 31.01.18.
 */
/**
 * Subscription interface
 * */
interface CompositeEventListener<TEvent> {
    /**
     * Subscribes listener
     * @param listener - listener to subscribe
     * */
    fun subscribe(listener: (TEvent) -> Unit): (TEvent) -> Unit

    /**
     * Unsubscribes listener
     * @param listener - listener to unsubscribe
     * */
    fun unSubscribe(listener: (TEvent) -> Unit): (TEvent) -> Unit

    /**
     * Unsubscribes all listeners
     * */
    fun unSubscribeAll()
}

/**
 * Extension function for [CompositeEventListener] witch return the first event which satisfies predicate
 * @param predicate - condition to select item
 * @return TEvent
 **/
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