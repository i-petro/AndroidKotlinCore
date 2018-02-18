package com.androidkotlincore.mvp.addons.impl

import com.androidkotlincore.mvp.addons.EmitableCompositeEventListener

/**
 * Created by Peter on 31.01.18.
 */
/**
 * [EmitableCompositeEventListener] wrapper, which emits last event after subscribing
 * Similar to rx BehaviourSubject.
 * */
class BehaviourCompositeEventListener<TEvent : Any>(
        private val delegate: EmitableCompositeEventListener<TEvent>)
    : EmitableCompositeEventListener<TEvent> by delegate {

    constructor() : this(SimpleCompositeEventListener())

    /**
     * Last emitted item
     * */
    private lateinit var event: TEvent

    /**
     * Notifies listeners about new event.
     * Saves last emitted event
     * @param event - event to notify about
     * */
    override fun emit(event: TEvent) {
        this.event = event
        this.delegate.emit(event)
    }

    /**
     * Adds listener to list and notifies it about last event if present
     * @param listener - new listener
     * */
    override fun subscribe(listener: (TEvent) -> Unit): (TEvent) -> Unit {
        if (this::event.isInitialized) {
            listener(event)
        }
        return delegate.subscribe(listener)
    }
}