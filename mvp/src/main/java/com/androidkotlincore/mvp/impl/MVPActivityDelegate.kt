package com.androidkotlincore.mvp.impl

import android.content.Context
import android.os.Build
import android.support.v7.app.AppCompatActivity
import com.androidkotlincore.mvp.MVPPresenter
import com.androidkotlincore.mvp.MVPView
import com.androidkotlincore.mvp.impl.permissions.OnRequestPermissionsResultEvent

/**
 * Created by Peter on 05.01.2018.
 */
/**
 * MVP delegate for [android.app.Activity]
 * */
class MVPActivityDelegate<TPresenter, TView, in V>(presentersStorage: PresentersStorage<TPresenter, TView>)
    : AbstractMVPDelegate<TPresenter, TView>(presentersStorage)

        where TPresenter : MVPPresenter<TPresenter, TView>,
              TView : MVPView<TView, TPresenter>,
              V : MVPView<TView, TPresenter>,
              V : AppCompatActivity {

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
        return view.isChangingConfigurations || !view.isFinishing
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
    override val contextNotNull: Context get() = view
}