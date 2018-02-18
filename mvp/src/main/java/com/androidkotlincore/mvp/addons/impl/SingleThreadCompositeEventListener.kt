package com.androidkotlincore.mvp.addons.impl

import android.os.Handler
import android.os.Looper
import com.androidkotlincore.mvp.addons.EmitableCompositeEventListener

/**
 * Created by Peter on 18.02.2018.
 */
/**
 * [EmitableCompositeEventListener] wrapper, which emits events in the specified [Looper]
 * As a result, each [TEvent] will be delivered in the single [Thread]
 * @param delegate - [Looper], associated with the [Thread]
 */
class SingleThreadCompositeEventListener<TEvent>(
        private val delegate: EmitableCompositeEventListener<TEvent>,
        looper: Looper = Looper.getMainLooper())
    : EmitableCompositeEventListener<TEvent> by delegate {

    private val handler = Handler(looper)

    /**
     * Notifies listeners about new event in the specified thread.
     * @param event - event to notify about
     * */
    override fun emit(event: TEvent) {
        handler.post { delegate.emit(event) }
    }
}