package com.androidkotlincore.mvp.impl

import android.arch.lifecycle.LifecycleRegistry
import android.arch.lifecycle.LifecycleRegistryOwner
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.annotation.CallSuper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.androidkotlincore.mvp.MVPPresenter
import com.androidkotlincore.mvp.MVPView
import com.androidkotlincore.mvp.ViewPersistenceStorage
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.archlifecycle.ControllerLifecycleRegistryOwner

/**
 * Created by Peter on 2/19/2018.
 */
@Suppress("LeakingThis", "DEPRECATION")
abstract class BaseMVPController<TView, TPresenter>(
        private val mvpDelegate: MVPControllerDelegate<TPresenter, TView, BaseMVPController<TView, TPresenter>>,
        args: Bundle)
    : Controller(args),
        MVPView<TView, TPresenter> by mvpDelegate,
        ViewPersistenceStorage,
        LifecycleRegistryOwner

        where TView : MVPView<TView, TPresenter>,
              TPresenter : MVPPresenter<TPresenter, TView> {

    constructor(args: Bundle) : this(MVPControllerDelegate(), args)

    private val lifecycleRegistryOwner = ControllerLifecycleRegistryOwner(this)
    abstract override val contextNotNull: Context
    override fun getLifecycle(): LifecycleRegistry = lifecycleRegistryOwner.lifecycle

    /**
     * Provides xml layout id
     * */
    abstract val layoutId: Int

    override val mvpTag: Bundle get() = persistenceArguments

    override val persistenceArguments: Bundle get() = args

    init {
        mvpDelegate.init(this)
        addLifecycleListener(object : LifecycleListener() {
            override fun postCreateView(controller: Controller, view: View) {
                onViewCreated(view)
            }
        })
    }

    /**
     * Delegates onActivityResult callback to [mvpDelegate]
     * */
    @CallSuper
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        mvpDelegate.onActivityResult(requestCode, resultCode, data)
    }

    /**
     * Delegates onRequestPermissionsResult callback to [mvpDelegate]
     * */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        mvpDelegate.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View =
            inflater.inflate(layoutId, container, false)


    open fun onViewCreated(view: View) {
    }
}