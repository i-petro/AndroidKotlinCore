package com.androidkotlincore.mvp.impl

import android.support.v4.app.BackStackAccessor
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.experimental.*
import kotlinx.coroutines.experimental.android.UI

//////////////////////////////////////// SUBSCRIPTIONS /////////////////////////////////////////////
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
fun Disposable.bind(subscriptionContainer: SubscriptionContainer): Disposable {
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

///////////////////////////////////////// COROUTINES ///////////////////////////////////////////////
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

////////////////////////////////////////////////////////////////////////////////////////////////////
val Fragment.isNeedToRetainInstance: Boolean
    get() {
        val localActivity = requireNotNull(activity) { "Activity must not be null for fragment $this"}

        if (localActivity.isChangingConfigurations) {
            return true
        }

        if (localActivity.isFinishing) {
            return false
        }

        if (BackStackAccessor.isFragmentOnBackStack(this)) {
            return true
        }

        return !isRemoving
    }
val AppCompatActivity.isNeedToRetainInstance: Boolean
    get() = isChangingConfigurations || !isFinishing

////////////////////////////////////////////////////////////////////////////////////////////////////