package com.androidkotlincore.mvp.impl

import kotlinx.coroutines.experimental.*
import kotlinx.coroutines.experimental.android.UI

interface SubscriptionContainer {
    fun addJob(job: Job): Job
    fun removeJob(job: Job): Job
    fun cancelAllSubscriptions()

    operator fun plusAssign(job: Job)
    operator fun minusAssign(job: Job)
}

open class SubscriptionsContainerDelegate : SubscriptionContainer {
    private val jobs = ArrayList<Job>()

    override fun addJob(job: Job): Job {
        jobs.add(job)
        return job
    }

    override fun removeJob(job: Job): Job {
        jobs.remove(job)
        return job
    }

    override fun cancelAllSubscriptions() {
        jobs.forEach { it.cancel() }
        jobs.clear()
    }

    override fun plusAssign(job: Job) {
        addJob(job)
    }

    override fun minusAssign(job: Job) {
        removeJob(job)
    }
}

////////////////////////////////////////////////////////////////////////////////////////////////////
fun Job.bind(subscriptionContainer: SubscriptionContainer): Job {
    subscriptionContainer += this
    return this
}

/**
 * Creates coroutine, which will be automatically added to the subscription container
 */
fun SubscriptionContainer.launchAsync(start: CoroutineStart = CoroutineStart.DEFAULT,
                                      block: suspend CoroutineScope.() -> Unit): Job =
        launch(CommonPool, start, block).bind(this)

/**
 * Creates coroutine, which will be automatically added to the subscription container
 */
fun SubscriptionContainer.launchUI(start: CoroutineStart = CoroutineStart.DEFAULT,
                                   block: suspend CoroutineScope.() -> Unit): Job =
        launch(UI, start, block).bind(this)

/**
 * Must be used ONLY when "this" is NOT instanceof SubscriptionContainer or you don't want to
 * automatically stop coroutine!
 */
fun launchCuroutineUI(start: CoroutineStart = CoroutineStart.DEFAULT,
                      block: suspend CoroutineScope.() -> Unit): Job = launch(UI, start, block)

fun <T> SubscriptionContainer.async(start: CoroutineStart = CoroutineStart.DEFAULT,
                                    block: suspend CoroutineScope.() -> T): Deferred<T> {
    val result = async(CommonPool, start, block)
    result.bind(this)
    return result
}

/**
 * Must be used ONLY when you have no access to SubscriptionContainer or you don't want to
 * automatically stop coroutine!
 */
fun <T> asyncCoroutine(start: CoroutineStart = CoroutineStart.DEFAULT,
                       block: suspend CoroutineScope.() -> T): Deferred<T> = async(CommonPool, start, block)
