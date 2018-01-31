package com.androidkotlincore.mvp

import android.arch.lifecycle.Lifecycle
import com.androidkotlincore.mvp.addons.CompositeEventListener
import com.androidkotlincore.mvp.impl.permissions.OnRequestPermissionsResultEvent

interface LifecycleOwner {
    val lifecycle: CompositeEventListener<Lifecycle.Event>
    val onActivityResult: CompositeEventListener<OnActivityResultEvent>
    val onRequestPermissionResult: CompositeEventListener<OnRequestPermissionsResultEvent>
}