package com.androidkotlincore.sample.app

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.provider.Settings
import com.androidkotlincore.sample.core.di.DI
import javax.inject.Inject

class AppInfoProviderImpl @Inject constructor(val context: Context) : AppInfoProvider {
    @SuppressLint("HardwareIds")
    private val androidIdVal = Settings.Secure.getString(DI.context.contentResolver, Settings.Secure.ANDROID_ID)
    private val manufacturer = Build.MANUFACTURER
    private val model = Build.MODEL
    private val deviceNameVal = if (model.startsWith(manufacturer)) model else manufacturer + " " + model

    override val androidId: String = androidIdVal

    override val deviceName: String = deviceNameVal
}