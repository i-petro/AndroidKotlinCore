package com.androidkotlincore.mvp.impl

import android.content.Intent
import android.os.Bundle
import android.support.annotation.CallSuper
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.androidkotlincore.mvp.MVPPresenter
import com.androidkotlincore.mvp.MVPView
import com.androidkotlincore.mvp.ViewPersistenceStorage

/**
 * Created by Peter on 06.01.2017.
 */
/**
 * Base class for MVPFragment.
 * @param mvpDelegate - implements functions from [MVPView]
 * */
@Suppress("LeakingThis")
abstract class BaseMVPFragment<TView, TPresenter>(private val mvpDelegate: MVPFragmentDelegate<TPresenter, TView, BaseMVPFragment<TView, TPresenter>>) :
        Fragment(),
        MVPView<TView, TPresenter> by mvpDelegate,
        ViewPersistenceStorage

        where TView : MVPView<TView, TPresenter>,
              TPresenter : MVPPresenter<TPresenter, TView> {

    constructor() : this(MVPFragmentDelegate())

    /**
     * Provides xml layout id
     * */
    abstract val layoutId: Int
    /**
     * @see [MVPView]
     * */
    override val mvpTag: Bundle get() = args
    /**
     * @see [ViewPersistenceStorage]
     * */
    override val args: Bundle
        get() {
            val result = requireNotNull(arguments) { "${this@BaseMVPFragment} must have NOT NULL arguments!" }
            require(result != Bundle.EMPTY) { "${this@BaseMVPFragment} must have NOT EMPTY arguments!" }
            return result
        }

    /**
     * mvpDelegate should be init here
     * */
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

    @CallSuper
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return if (layoutId != 0) inflater.inflate(layoutId, container, false)
        else super.onCreateView(inflater, container, savedInstanceState)
    }
}