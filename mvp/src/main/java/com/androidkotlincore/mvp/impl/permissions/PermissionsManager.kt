package com.androidkotlincore.mvp.impl.permissions

/**
 * Created by Peter on 19.01.18.
 */
/**
 * Interface to work with permissions
 * */
interface PermissionsManager {
    /**
     * Asks about specified permissions
     *
     * @param permissions - list of permissions to request
     * @return [RequestPermissionsResult]
     * */
    suspend fun requestPermissions(vararg permissions: String) = requestPermissions(permissions.toList())

    /**
     * Asks about specified permissions
     *
     * @param permissions - list of permissions to request
     * @return [RequestPermissionsResult]
     * */
    suspend fun requestPermissions(permissions: List<String>): RequestPermissionsResult

    /**
     * Asks about specified permissions. Throws exception if not all permissions were granted
     *
     * @param permissions - list of permissions to request
     * @return [RequestPermissionsResult]
     * */
    suspend fun requestPermissionsOrThrow(vararg permissions: String) = requestPermissionsOrThrow(permissions.toList())

    /**
     * Asks about specified permissions. Throws exception if not all permissions were granted
     *
     * @param permissions - list of permissions to request
     * @return [RequestPermissionsResult]
     * */
    suspend fun requestPermissionsOrThrow(permissions: List<String>): RequestPermissionsResult

    /**
     * Checks if [permission] needs explanation dialog
     *
     * @param permission - permission to check
     * @return true if [permission] needs explanation dialog
     * */
    suspend fun shouldShowRequestPermissionRationale(permission: String): Boolean
}