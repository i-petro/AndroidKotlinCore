package com.androidkotlincore.mvpconductor

import android.content.Intent
import android.os.Bundle
import android.support.annotation.CallSuper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.androidkotlincore.mvp.MVPPresenter
import com.androidkotlincore.mvp.MVPView
import com.androidkotlincore.mvp.ViewPersistenceStorage
import com.bluelinelabs.conductor.archlifecycle.LifecycleController

/**
 * Created by pilc on 2/19/2018.
 */
@Suppress("LeakingThis")
abstract class BaseMVPController<TView, TPresenter>(private val mvpDelegate: MVPControllerDelegate<TPresenter, TView, BaseMVPController<TView, TPresenter>>)
    : LifecycleController(),
        MVPView<TView, TPresenter> by mvpDelegate,
        ViewPersistenceStorage

        where TView : MVPView<TView, TPresenter>,
              TPresenter : MVPPresenter<TPresenter, TView> {

    constructor() : this(MVPControllerDelegate())


    /**
     * Provides xml layout id
     * */
    abstract val layoutId: Int

    override val mvpTag: Bundle get() = persistenceArguments

    override val persistenceArguments: Bundle get() = args

    init {
        mvpDelegate.init(this)
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
}