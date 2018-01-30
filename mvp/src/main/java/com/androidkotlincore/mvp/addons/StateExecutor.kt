package com.androidkotlincore.mvp.addons

import java.util.concurrent.ConcurrentLinkedQueue
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * State executor. Executes an action when executionPredicate returns true; otherwise saves an action
 * to the queue and executes it when next time
 */
class StateExecutor<State : Any?, out OutState : Any?> : ReadWriteProperty<Any?, State> {
    var value: State
        set(value) {
            field = value
            if (actionsQueue.isNotEmpty() && executionPredicate.invoke(value)) {
                while (actionsQueue.isNotEmpty()) {
                    actionsQueue.poll().invoke(transformer.invoke(value))
                }
            }
        }
    private val actionsQueue = ConcurrentLinkedQueue<(OutState) -> Unit>()
    private val executionPredicate: ((state: State) -> Boolean)
    private val transformer: ((inState: State) -> OutState)

    constructor(initState: State, executionPredicate: ((state: State) -> Boolean)) {
        this.executionPredicate = executionPredicate
        this.value = initState
        this.transformer = { value as OutState }
    }

    constructor(initState: State, executionPredicate: ((state: State) -> Boolean), transformer: ((inState: State) -> OutState)) {
        this.executionPredicate = executionPredicate
        this.transformer = transformer
        this.value = initState
    }

    override fun getValue(thisRef: Any?, property: KProperty<*>): State = value

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: State) = run { this.value = value }

    fun invoke(action: (state: OutState) -> Unit) {
        if (executionPredicate.invoke(value)) action.invoke(transformer.invoke(value))
        else actionsQueue.add(action)
    }

    fun actionsCount() = actionsQueue.size
    fun clear() = actionsQueue.clear()
}