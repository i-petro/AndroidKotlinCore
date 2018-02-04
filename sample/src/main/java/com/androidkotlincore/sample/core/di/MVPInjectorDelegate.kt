package com.androidkotlincore.sample.core.di

import android.os.Bundle
import com.androidkotlincore.mvp.MVPInjector
import com.androidkotlincore.mvp.MVPPresenter
import com.androidkotlincore.mvp.MVPView
import com.androidkotlincore.sample.presentation.screens.container.FragmentContainerActivity
import com.androidkotlincore.sample.presentation.screens.container.FragmentContainerPresenterImpl

/**
 * Created by Peter on 04.02.2018.
 */
class MVPInjectorDelegate : MVPInjector {
    override fun createPresenter(viewJavaClass: Class<in MVPView<*, *>>, mvpTag: Bundle): MVPPresenter<*, *> =
            when (viewJavaClass) {
                FragmentContainerActivity::class.java -> FragmentContainerPresenterImpl()
                else -> throwPresenterNotFound(viewJavaClass, mvpTag)
            }

    private fun throwPresenterNotFound(viewJavaClass: Class<in MVPView<*, *>>, mvpTag: Bundle): Nothing {
        throw IllegalArgumentException("Illegal argument viewJavaClass $viewJavaClass, mvpTag: $mvpTag")
    }
}