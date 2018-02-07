package com.androidkotlincore.mvp.addons

/**
 * Created by Peter on 05.02.2018.
 */
interface EmitableCompositeEventListener<TEvent> : CompositeEventListener<TEvent> {
    fun emit(event: TEvent)
}