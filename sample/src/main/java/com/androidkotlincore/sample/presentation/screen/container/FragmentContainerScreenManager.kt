package com.androidkotlincore.sample.presentation.screen.container

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment

/**
 * Created by Peter on 05.02.2018.
 */
interface FragmentContainerScreenManager {
    fun <F : Fragment> start(context: Context, clazz: Class<F>, arguments: Bundle = Bundle())
}