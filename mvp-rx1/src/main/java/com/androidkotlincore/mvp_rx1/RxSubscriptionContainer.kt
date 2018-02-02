package com.androidkotlincore.mvp_rx1

import com.androidkotlincore.mvp.impl.SubscriptionContainer
import com.androidkotlincore.mvp.impl.SubscriptionsContainerDelegate
import rx.Subscription
import rx.subscriptions.CompositeSubscription

/**
 * Created by Peter on 31.01.2018.
 */
interface RxSubscriptionContainer : SubscriptionContainer {
    fun addSubscription(rxSubscription: Subscription): Subscription
    fun removeSubscription(rxSubscription: Subscription): Subscription
    override fun cancelAllSubscriptions()

    operator fun plusAssign(rxSubscription: Subscription)
    operator fun minusAssign(rxSubscription: Subscription)
}

////////////////////////////////////////////////////////////////////////////////////////////////////

class RxSubscriptionsContainerDelegate: SubscriptionsContainerDelegate(), RxSubscriptionContainer {
    private val subscriptions = CompositeSubscription()

    override fun cancelAllSubscriptions() {
        super.cancelAllSubscriptions()
        subscriptions.clear()
    }

    override fun addSubscription(rxSubscription: Subscription): Subscription {
        subscriptions.add(rxSubscription)
        return rxSubscription
    }

    override fun removeSubscription(rxSubscription: Subscription): Subscription {
        subscriptions.remove(rxSubscription)
        return rxSubscription
    }

    override fun plusAssign(rxSubscription: Subscription) {
        addSubscription(rxSubscription)
    }

    override fun minusAssign(rxSubscription: Subscription) {
        removeSubscription(rxSubscription)
    }
}

////////////////////////////////////////////////////////////////////////////////////////////////////
/**
 * Adds current subscription to the composite subscription
 */
operator fun CompositeSubscription.plusAssign(subscription: Subscription) {
    add(subscription)
}

/**
 * Adds current subscription to the subscription container
 * Always use this method to prevent memory leaks!
 */
fun Subscription.bind(subscriptionContainer: RxSubscriptionContainer): Subscription {
    subscriptionContainer += this
    return this
}

/**
 * Adds current composite subscription to the subscription container
 * Always use this method to prevent memory leaks!
 */
fun Subscription.bind(compositeSubscription: CompositeSubscription): Subscription {
    compositeSubscription += this
    return this
}