package com.androidkotlincore.mvp.addons.impl

import com.androidkotlincore.mvp.addons.CompositeEventListener
import com.androidkotlincore.mvp.addons.EmitableCompositeEventListener
import java.util.concurrent.CopyOnWriteArrayList


/**
 * Created by Peter on 31.01.18.
 */
/**
 * Simple realisation of Observer patter
 * */
class SimpleCompositeEventListener<TEvent> : CompositeEventListener<TEvent>, EmitableCompositeEventListener<TEvent> {
    /**
     * Thread-safe list of listeners
     * */
    private val listeners = CopyOnWriteArrayList<(TEvent) -> Unit>()

    /**
     * Notifies listeners about new event
     * @param event - event to notify about
     * */
    override fun emit(event: TEvent) {
        listeners.forEach { it(event) }
    }

    /**
     * Adds listener to list
     * @param listener - new listener
     * */
    override fun subscribe(listener: (TEvent) -> Unit): (TEvent) -> Unit {
        listeners.add(listener)
        return listener
    }

    /**
     * Removes listener from list
     * @param listener - listener to remove
     * */
    override fun unSubscribe(listener: (TEvent) -> Unit): (TEvent) -> Unit {
        listeners.remove(listener)
        return listener
    }

    /**
     * Removes all listeners from list
     * */
    override fun unSubscribeAll() {
        listeners.clear()
    }
}