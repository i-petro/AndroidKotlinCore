package com.androidkotlincore.mvp.impl.permissions

/**
 * Created by Peter on 19.01.18.
 */
interface PermissionsManager {
    suspend fun requestPermissions(vararg permissions: String) = requestPermissions(permissions.toList())
    suspend fun requestPermissions(permissions: List<String>): RequestPermissionsResult
    suspend fun requestPermissionsOrThrow(vararg permissions: String) = requestPermissionsOrThrow(permissions.toList())
    suspend fun requestPermissionsOrThrow(permissions: List<String>): RequestPermissionsResult
    suspend fun shouldShowRequestPermissionRationale(permission: String): Boolean
}