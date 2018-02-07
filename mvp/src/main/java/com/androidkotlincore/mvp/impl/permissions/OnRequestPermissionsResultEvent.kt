@file:Suppress("MemberVisibilityCanBePrivate")

package com.androidkotlincore.mvp.impl.permissions

import android.content.pm.PackageManager

/**
 * Created by Peter on 19.01.18.
 */
/**
 * Raw data from [android.support.v4.app.FragmentActivity.onRequestPermissionsResult] or
 * [android.support.v4.app.Fragment.onRequestPermissionsResult]
 *
 * @param requestCode - permissions request code
 * @param permissions - list of requested permissions
 * @param grantResults - the grant results for the corresponding permissions
 * @param shouldShowRequestPermissionRationale - says whether you should show UI with rationale
 * for every requesting a permission
 * */
class OnRequestPermissionsResultEvent(val requestCode: Int, val permissions: List<String>, val grantResults: List<Int>,
                                      val shouldShowRequestPermissionRationale: List<Boolean>) {
    /**
     * Returns true if all permissions ara granted
     * */
    val isAllGranted get() = grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }
    /**
     * Returns true if specific permission is granted
     *
     * @param permission - permission to check
     * */
    fun isGranted(permission: String): Boolean = grantResults[permissions.indexOf(permission)] == PackageManager.PERMISSION_GRANTED
    /**
     * Returns true if specific permission needs explanation
     *
     * @param permission - permission to check
     * */
    fun isShouldShowRequestPermissionRationale(permission: String): Boolean = shouldShowRequestPermissionRationale[permissions.indexOf(permission)]
}