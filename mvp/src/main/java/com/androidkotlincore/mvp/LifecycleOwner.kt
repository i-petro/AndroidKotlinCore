package com.androidkotlincore.mvp

import android.arch.lifecycle.Lifecycle
import com.androidkotlincore.mvp.impl.permissions.OnRequestPermissionsResultEvent
import io.reactivex.Observable

interface LifecycleOwner {
    val lifecycle: Observable<Lifecycle.Event>
    val onActivityResult: Observable<OnActivityResultEvent>
    val onRequestPermissionResult: Observable<OnRequestPermissionsResultEvent>
}