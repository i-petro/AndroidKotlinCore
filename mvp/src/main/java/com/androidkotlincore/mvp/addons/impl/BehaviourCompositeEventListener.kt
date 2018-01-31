package com.androidkotlincore.mvp.addons.impl

import com.androidkotlincore.mvp.addons.CompositeEventListener
import java.util.concurrent.CopyOnWriteArrayList

/**
 * Created by Peter on 31.01.18.
 */
class BehaviourCompositeEventListener<TEvent> : CompositeEventListener<TEvent> {
    private val listeners = CopyOnWriteArrayList<(TEvent) -> Unit>()
    private var event: TEvent? = null

    override fun emit(event: TEvent) {
        this.event = event
        this.listeners.forEach { it.invoke(event) }
    }

    override fun subscribe(listener: (TEvent) -> Unit): (TEvent) -> Unit {
        this.listeners.add(listener)
        this.event?.let { listener.invoke(it) }
        return listener
    }

    override fun unSubscribe(listener: (TEvent) -> Unit): (TEvent) -> Unit {
        this.listeners.remove(listener)
        return listener
    }

    override fun unSubscribeAll() {
        this.listeners.clear()
    }
}