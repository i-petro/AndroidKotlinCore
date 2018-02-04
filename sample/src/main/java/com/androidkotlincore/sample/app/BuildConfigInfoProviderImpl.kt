package com.androidkotlincore.sample.app

import android.os.Build
import com.androidkotlincore.sample.BuildConfig
import javax.inject.Inject

class BuildConfigInfoProviderImpl @Inject constructor() : BuildConfigInfoProvider {

    override val isDebug: Boolean = BuildConfig.DEBUG

    override val applicationId: String = BuildConfig.APPLICATION_ID

    override val buildType: String = BuildConfig.BUILD_TYPE

    override val versionCode: Int = BuildConfig.VERSION_CODE

    override val versionName: String = BuildConfig.VERSION_NAME

    override val osVersion: String = Build.VERSION.RELEASE


}
