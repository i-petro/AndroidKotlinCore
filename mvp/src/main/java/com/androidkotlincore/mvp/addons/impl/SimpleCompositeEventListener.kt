package com.androidkotlincore.mvp.addons.impl

import com.androidkotlincore.mvp.addons.CompositeEventListener
import java.util.concurrent.CopyOnWriteArrayList


/**
 * Created by Peter on 31.01.18.
 */
class SimpleCompositeEventListener<TEvent> : CompositeEventListener<TEvent> {
    private val listeners = CopyOnWriteArrayList<(TEvent) -> Unit>()

    override fun emit(event: TEvent) {
        listeners.forEach { it.invoke(event) }
    }

    override fun subscribe(listener: (TEvent) -> Unit): (TEvent) -> Unit {
        listeners.add(listener)
        return listener
    }

    override fun unSubscribe(listener: (TEvent) -> Unit): (TEvent) -> Unit {
        listeners.remove(listener)
        return listener
    }

    override fun unSubscribeAll() {
        listeners.clear()
    }
}