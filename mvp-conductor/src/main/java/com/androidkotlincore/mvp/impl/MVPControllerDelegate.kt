package com.androidkotlincore.mvp.impl

import android.arch.lifecycle.LifecycleRegistryOwner
import android.content.Context
import android.os.Build
import com.androidkotlincore.mvp.MVPPresenter
import com.androidkotlincore.mvp.MVPView
import com.androidkotlincore.mvp.impl.permissions.OnRequestPermissionsResultEvent
import com.bluelinelabs.conductor.Controller

/**
* Created by Peter on 2/19/2018.
*/

/**
 * MVP delegate for [android.support.v4.app.Fragment]
 * */
class MVPControllerDelegate<TPresenter, TView, in V>(presentersStorage: PresentersStorage<TPresenter, TView>)
    : AbstractMVPDelegate<TPresenter, TView>(presentersStorage)

        where TPresenter : MVPPresenter<TPresenter, TView>,
              TView : MVPView<TView, TPresenter>,
              V : MVPView<TView, TPresenter>,
              V : Controller,
              V : LifecycleRegistryOwner {

    constructor() : this(PresentersStorageImpl())

    private lateinit var view: V

    /**
     * @see [AbstractMVPDelegate.init]
     * */
    fun init(view: V) {
        this.view = view
        super.init(view)
    }

    /**
     * @see [AbstractMVPDelegate.retainPresenterInstance]
     * */
    override fun retainPresenterInstance(): Boolean {
        //TODO: test cases with screen rotation, don't keep activities mode
        return !view.isBeingDestroyed && !view.isDestroyed
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        val shouldShowRequestPermissionRationale = BooleanArray(permissions.size) {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && view.shouldShowRequestPermissionRationale(permissions[it])
        }

        onRequestPermissionResultEmitter.emit(OnRequestPermissionsResultEvent(
                requestCode,
                permissions.toList(),
                grantResults.toList(),
                shouldShowRequestPermissionRationale.toList()))
    }

    /**
     * Provides not null context
     * */
    override val contextNotNull: Context
        get() = view.applicationContext ?: throw IllegalStateException("Non nullable context not supported for $view")
}