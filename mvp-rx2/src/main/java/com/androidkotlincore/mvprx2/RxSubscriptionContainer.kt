package com.androidkotlincore.mvprx2

import com.androidkotlincore.mvp.impl.SubscriptionContainer
import com.androidkotlincore.mvp.impl.SubscriptionsContainerDelegate
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * Created by Peter on 31.01.2018.
 */
interface RxSubscriptionContainer : SubscriptionContainer {
    fun addSubscription(rxSubscription: Disposable): Disposable
    fun removeSubscription(rxSubscription: Disposable): Disposable
    override fun cancelAllSubscriptions()

    operator fun plusAssign(rxSubscription: Disposable)
    operator fun minusAssign(rxSubscription: Disposable)
}

////////////////////////////////////////////////////////////////////////////////////////////////////

class RxSubscriptionsContainerDelegate: SubscriptionsContainerDelegate(),  RxSubscriptionContainer {
    private val subscriptions = CompositeDisposable()

    override fun cancelAllSubscriptions() {
        super.cancelAllSubscriptions()
        subscriptions.clear()
    }

    override fun addSubscription(rxSubscription: Disposable): Disposable {
        subscriptions.add(rxSubscription)
        return rxSubscription
    }

    override fun removeSubscription(rxSubscription: Disposable): Disposable {
        subscriptions.remove(rxSubscription)
        return rxSubscription
    }

    override fun plusAssign(rxSubscription: Disposable) {
        addSubscription(rxSubscription)
    }

    override fun minusAssign(rxSubscription: Disposable) {
        removeSubscription(rxSubscription)
    }
}

////////////////////////////////////////////////////////////////////////////////////////////////////
/**
 * Adds current subscription to the composite subscription
 */
operator fun CompositeDisposable.plusAssign(subscription: Disposable) {
    add(subscription)
}

/**
 * Adds current subscription to the subscription container
 * Always use this method to prevent memory leaks!
 */
fun Disposable.bind(subscriptionContainer: RxSubscriptionContainer): Disposable {
    subscriptionContainer += this
    return this
}

/**
 * Adds current composite subscription to the subscription container
 * Always use this method to prevent memory leaks!
 */
fun Disposable.bind(compositeSubscription: CompositeDisposable): Disposable {
    compositeSubscription += this
    return this
}