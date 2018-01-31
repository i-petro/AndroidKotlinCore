@file:Suppress("MemberVisibilityCanBePrivate")

package com.androidkotlincore.mvp.impl.permissions

import android.content.pm.PackageManager

/**
 * Created by Peter on 19.01.18.
 */
class OnRequestPermissionsResultEvent(val requestCode: Int, val permissions: List<String>, val grantResults: List<Int>, val shouldShowRequestPermissionRationale: List<Boolean>) {
    val isAllGranted get() = grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }
    fun isGranted(permission: String): Boolean = grantResults[permissions.indexOf(permission)] == PackageManager.PERMISSION_GRANTED
    fun isShouldShowRequestPermissionRationale(permission: String): Boolean = shouldShowRequestPermissionRationale[permissions.indexOf(permission)]
}