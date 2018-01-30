package com.androidkotlincore.mvp.impl

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.experimental.Job

interface SubscriptionContainer {
    fun addSubscription(rxSubscription: Disposable): Disposable
    fun addJob(job: Job): Job
    fun removeSubscription(rxSubscription: Disposable): Disposable
    fun removeJob(job: Job): Job
    fun clearSubscriptionsAndJobs()

    operator fun plusAssign(rxSubscription: Disposable)
    operator fun plusAssign(job: Job)
    operator fun minusAssign(rxSubscription: Disposable)
    operator fun minusAssign(job: Job)
}

class SubscriptionsContainerDelegate : SubscriptionContainer {
    private val subs = CompositeDisposable()
    private val jobs = ArrayList<Job>()

    override fun addSubscription(rxSubscription: Disposable): Disposable {
        subs.add(rxSubscription)
        return rxSubscription
    }

    override fun addJob(job: Job): Job {
        jobs.add(job)
        return job
    }

    override fun removeSubscription(rxSubscription: Disposable): Disposable {
        subs.remove(rxSubscription)
        return rxSubscription
    }

    override fun removeJob(job: Job): Job {
        jobs.remove(job)
        return job
    }

    override fun clearSubscriptionsAndJobs() {
        subs.clear()
        jobs.forEach { it.cancel() }
        jobs.clear()
    }

    override fun plusAssign(rxSubscription: Disposable) {
        addSubscription(rxSubscription)
    }

    override fun plusAssign(job: Job) {
        addJob(job)
    }

    override fun minusAssign(rxSubscription: Disposable) {
        removeSubscription(rxSubscription)
    }

    override fun minusAssign(job: Job) {
        removeJob(job)
    }
}
