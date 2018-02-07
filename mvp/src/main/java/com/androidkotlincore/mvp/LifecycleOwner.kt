package com.androidkotlincore.mvp

import android.arch.lifecycle.Lifecycle
import com.androidkotlincore.mvp.addons.CompositeEventListener
import com.androidkotlincore.mvp.impl.permissions.OnRequestPermissionsResultEvent

/**
 * Represents a [MVPView] lifecycle and other events owner
 * @property lifecycle simple [Lifecycle.Event]'s listener
 * @property onActivityResult onActivityResult() events listener
 * @property onRequestPermissionResult onRequestPermissionResult events listener
 */
interface LifecycleOwner {
    val lifecycle: CompositeEventListener<Lifecycle.Event>
    val onActivityResult: CompositeEventListener<OnActivityResultEvent>
    val onRequestPermissionResult: CompositeEventListener<OnRequestPermissionsResultEvent>
}