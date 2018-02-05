package com.androidkotlincore.mvp.impl

import android.content.Intent
import android.os.Bundle
import android.support.annotation.CallSuper
import android.support.v7.app.AppCompatActivity
import com.androidkotlincore.mvp.MVPPresenter
import com.androidkotlincore.mvp.MVPView
import com.androidkotlincore.mvp.ViewPersistenceStorage

/**
 * Created by Peter on 06.01.2017.
 */
@Suppress("LeakingThis")
abstract class BaseMVPActivity<TView, TPresenter>(
        private val mvpDelegate: MVPActivityDelegate<
                TPresenter,
                TView,
                BaseMVPActivity<TView, TPresenter>>) :

        AppCompatActivity(),
        MVPView<TView, TPresenter> by mvpDelegate,
        ViewPersistenceStorage

        where TView : MVPView<TView, TPresenter>,
              TPresenter : MVPPresenter<TPresenter, TView> {

    constructor() : this(MVPActivityDelegate())

    abstract val layoutId: Int

    override lateinit var args: Bundle

    init {
        mvpDelegate.init(this)
    }

    @CallSuper
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        mvpDelegate.onActivityResult(requestCode, resultCode, data)
    }

    @CallSuper
    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        mvpDelegate.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        args = savedInstanceState?.getBundle(EXTRA_ARGS) ?: Bundle()

        super.onCreate(savedInstanceState)
        setContentView(layoutId)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBundle(EXTRA_ARGS, args)
    }

    private companion object {
        private val EXTRA_ARGS = "EXTRA_ARGS_${BaseMVPActivity::class.java.name}"
    }
}