package com.androidkotlincore.mvp.impl

import kotlinx.coroutines.experimental.*
import kotlinx.coroutines.experimental.android.UI

/**
 * Container for subscriptions.
 * Should be cleared in [com.androidkotlincore.mvp.MVPPresenter.onDestroyed] or in similar methods
 * */
interface SubscriptionContainer {
    /**
     * Adds job to container
     * @param job - coroutine job; [Job]
     * @return itself
     * */
    fun addJob(job: Job): Job

    /**
     * Removes job from container
     * @param job - coroutine job; [Job]
     * @return itself
     * */
    fun removeJob(job: Job): Job

    /**
     * Clears job container
     * */
    fun cancelAllSubscriptions()

    /**
     * Overloads plus operator to add job into container
     * @param job - [Job]
     * */
    operator fun plusAssign(job: Job)

    /**
     * Overloads minus operator to remove job from container
     * @param job - [Job]
     * */
    operator fun minusAssign(job: Job)
}

/**
 * Simple [SubscriptionContainer] implementation
 */
open class SubscriptionsContainerDelegate : SubscriptionContainer {
    private val jobs = ArrayList<Job>()

    override fun addJob(job: Job): Job = job.also { jobs.add(it) }

    override fun removeJob(job: Job): Job = job.also { jobs.remove(it) }

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
/**
 * Extension to add job into container
 * @param subscriptionContainer - [SubscriptionContainer]
 * @return [Job]
 * */
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
