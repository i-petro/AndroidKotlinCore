package com.androidkotlincore.mvp.impl

import android.content.Context
import android.os.Build
import android.support.v4.app.BackStackAccessor
import android.support.v4.app.Fragment
import com.androidkotlincore.mvp.MVPPresenter
import com.androidkotlincore.mvp.MVPView
import com.androidkotlincore.mvp.impl.permissions.OnRequestPermissionsResultEvent

/**
 * Created by Peter on 05.01.2018.
 */
/**
 * MVP delegate for [android.support.v4.app.Fragment]
 * */
class MVPFragmentDelegate<TPresenter, TView, in V>(presentersStorage: PresentersStorage<TPresenter, TView>)
    : AbstractMVPDelegate<TPresenter, TView>(presentersStorage)

        where TPresenter : MVPPresenter<TPresenter, TView>,
              TView : MVPView<TView, TPresenter>,
              V : MVPView<TView, TPresenter>,
              V : Fragment {

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
        val localActivity = requireNotNull(view.activity) { "Activity must not be null for fragment $this" }

        if (localActivity.isChangingConfigurations) {
            return true
        }

        if (localActivity.isFinishing) {
            return false
        }

        if (BackStackAccessor.isFragmentOnBackStack(view)) {
            return true
        }

        return !view.isRemoving
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
        get() = view.context ?: throw IllegalStateException("Non nullable context not supported for $view")
}