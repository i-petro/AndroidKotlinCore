package com.androidkotlincore.mvp.impl

import android.content.Intent
import android.os.Bundle
import android.support.annotation.CallSuper
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.androidkotlincore.mvp.MVPPresenter
import com.androidkotlincore.mvp.MVPView
import com.androidkotlincore.mvp.ViewPersistenceStorage

/**
 * Created by Peter on 06.01.2017.
 */
@Suppress("LeakingThis")
abstract class BaseMVPDialog<TView, TPresenter>(private val mvpDelegate: MVPFragmentDelegate<TPresenter, TView, BaseMVPDialog<TView, TPresenter>>) :
        DialogFragment(),
        MVPView<TView, TPresenter> by mvpDelegate,
        ViewPersistenceStorage

        where TView : MVPView<TView, TPresenter>,
              TPresenter : MVPPresenter<TPresenter, TView> {

    constructor() : this(MVPFragmentDelegate())

    abstract val layoutId: Int
    override val mvpTag: Bundle get() = args
    override val args: Bundle
        get() {
            val result = requireNotNull(arguments) { "${this@BaseMVPDialog} must have NOT NULL arguments!" }
            require(result != Bundle.EMPTY) { "${this@BaseMVPDialog} must have NOT EMPTY arguments!" }
            return result
        }

    init {
        mvpDelegate.init(this)
    }

    @CallSuper
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        mvpDelegate.onActivityResult(requestCode, resultCode, data)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        mvpDelegate.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    @CallSuper
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return if (layoutId != 0) inflater.inflate(layoutId, container, false)
        else super.onCreateView(inflater, container, savedInstanceState)
    }

    @CallSuper
    override fun onDestroyView() {
        if (dialog != null && retainInstance) {
            dialog.setDismissMessage(null)
        }
        super.onDestroyView()
    }
}