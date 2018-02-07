package com.androidkotlincore.mvp.addons

/**
 * Created by Peter on 05.02.2018.
 */
/**
 * [CompositeEventListener] which allows to emit events
 * */
interface EmitableCompositeEventListener<TEvent> : CompositeEventListener<TEvent> {
    /**
     * Emits new event. Sends it to listeners
     * @param event - event to emit
     * */
    fun emit(event: TEvent)
}