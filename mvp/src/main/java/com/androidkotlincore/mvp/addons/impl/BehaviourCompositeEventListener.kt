package com.androidkotlincore.mvp.addons.impl

import com.androidkotlincore.mvp.addons.CompositeEventListener
import com.androidkotlincore.mvp.addons.EmitableCompositeEventListener
import java.util.concurrent.CopyOnWriteArrayList

/**
 * Created by Peter on 31.01.18.
 */
/**
 * Realisation of Observer pattern similar to rx BehaviourSubject.
 * Emits last emitted event to new subscribed listener
 * */
class BehaviourCompositeEventListener<TEvent> : CompositeEventListener<TEvent>, EmitableCompositeEventListener<TEvent> {
    /**
     * Thread-safe list of listeners
     * */
    private val listeners = CopyOnWriteArrayList<(TEvent) -> Unit>()
    /**
     * Last emitted item
     * */
    private var event: TEvent? = null

    /**
     * Notifies listeners about new event.
     * Saves last emitted event
     * @param event - event to notify about
     * */
    override fun emit(event: TEvent) {
        this.event = event
        this.listeners.forEach { it.invoke(event) }
    }

    /**
     * Adds listener to list and notifies it about last event if present
     * @param listener - new listener
     * */
    override fun subscribe(listener: (TEvent) -> Unit): (TEvent) -> Unit {
        this.listeners.add(listener)
        this.event?.let { listener.invoke(it) }
        return listener
    }

    /**
     * Removes listener from list
     * @param listener - listener to remove
     * */
    override fun unSubscribe(listener: (TEvent) -> Unit): (TEvent) -> Unit {
        this.listeners.remove(listener)
        return listener
    }

    /**
     * Removes all listeners from list
     * */
    override fun unSubscribeAll() {
        this.listeners.clear()
    }
}