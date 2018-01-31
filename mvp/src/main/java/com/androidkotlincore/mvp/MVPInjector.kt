package com.androidkotlincore.mvp

import android.os.Bundle

/**
 * Created by Peter on 07.01.2017.
 */
interface MVPInjector {
    /**
     * This method must returns new MVPPresenter for MVPView
     */
    fun createPresenter(viewJavaClass: Class<in MVPView<*, *>>, mvpTag: Bundle): MVPPresenter<*, *>
}