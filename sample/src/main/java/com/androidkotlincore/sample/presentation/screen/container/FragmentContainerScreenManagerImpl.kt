package com.androidkotlincore.sample.presentation.screen.container

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import javax.inject.Inject

class FragmentContainerScreenManagerImpl @Inject constructor() : FragmentContainerScreenManager {
    override fun <F : Fragment> start(context: Context, clazz: Class<F>, arguments: Bundle) {
        val intent = Intent(context, FragmentContainerActivity::class.java)
                .putExtra(FragmentContainerActivity.EXTRA_FRAGMENT_CLASS, clazz)
                .putExtra(FragmentContainerActivity.EXTRA_FRAGMENT_ARGUMENTS, arguments)
        context.startActivity(intent)
    }
}