package com.androidkotlincore.mvp.addons

import java.util.concurrent.ConcurrentLinkedQueue
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * State executor. Executes an action when executionPredicate returns true; otherwise saves an action
 * to the queue and executes it later
 * @property value - state to be transformed into [OutState]
 * @property actionsQueue - thread-safe queue of actions
 * @property executionPredicate - predicate which says when action can be executed
 * @property transformer - function that receives [State] and returns [OutState]
 * @constructor creates [StateExecutor]
 */
class StateExecutor<State : Any?, out OutState : Any?> : ReadWriteProperty<Any?, State> {
    var value: State
        set(value) {
            field = value
            if (actionsQueue.isNotEmpty() && executionPredicate(value)) {
                while (actionsQueue.isNotEmpty()) {
                    actionsQueue.poll().invoke(transformer(value))
                }
            }
        }
    private val actionsQueue = ConcurrentLinkedQueue<(OutState) -> Unit>()
    private val executionPredicate: ((state: State) -> Boolean)
    private val transformer: ((inState: State) -> OutState)

    constructor(initState: State, executionPredicate: ((state: State) -> Boolean)) {
        this.executionPredicate = executionPredicate
        this.value = initState
        this.transformer = {
            @Suppress("UNCHECKED_CAST")
            value as OutState
        }
    }

    constructor(initState: State, executionPredicate: ((state: State) -> Boolean), transformer: ((inState: State) -> OutState)) {
        this.executionPredicate = executionPredicate
        this.transformer = transformer
        this.value = initState
    }

    /**
     * @see [ReadWriteProperty.getValue]
     * */
    override fun getValue(thisRef: Any?, property: KProperty<*>): State = value

    /**
     * @see [ReadWriteProperty.setValue]
     * */
    override fun setValue(thisRef: Any?, property: KProperty<*>, value: State) = run { this.value = value }

    /**
     * Executes action or adds it to [actionsQueue]. It depends on [executionPredicate]
     * @param action - action to invoke
     * */
    operator fun invoke(action: (state: OutState) -> Unit) {
        if (executionPredicate(value)) action(transformer(value))
        else actionsQueue.add(action)
    }

    /**
     * Return [actionsQueue] length
     * */
    fun actionsCount() = actionsQueue.size

    /**
     * Clear [actionsQueue]
     * */
    fun clear() = actionsQueue.clear()
}