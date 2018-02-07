package com.androidkotlincore.mvp.impl.permissions

/**
 * Created by Peter on 19.01.18.
 */
/**
 * Contains information about permissions response.
 * Is a wrapper for [OnRequestPermissionsResultEvent]
 *
 * @property grantedPermissions - list of granted permissions
 * @property deniedPermissions - list of denied permissions
 * @property event - [OnRequestPermissionsResultEvent], raw data from onRequestPermissionsResult method
 * @property isAllGranted - returns true if [deniedPermissions] is empty
 * */
class RequestPermissionsResult {
    val grantedPermissions: List<String>
    val deniedPermissions: List<String>
    private val event: OnRequestPermissionsResultEvent?

    /**
     * Creates RequestPermissionsResult from lists of granted permissions and denied permissions
     * */
    constructor(grantedPermissions: List<String>, deniedPermissions: List<String>) {
        this.grantedPermissions = grantedPermissions
        this.deniedPermissions = deniedPermissions
        this.event = null
    }

    /**
     * Creates RequestPermissionsResult from [OnRequestPermissionsResultEvent]
     * */
    constructor(event: OnRequestPermissionsResultEvent) {
        this.event = event
        this.grantedPermissions = event.permissions.filter { event.isGranted(it) }
        this.deniedPermissions = event.permissions.filter { !event.isGranted(it) }
    }

    val isAllGranted get() = deniedPermissions.isEmpty()
    /**
     * Returns true if specific permission is granted
     *
     * @param permission - permission to check
     * */
    fun isGranted(permission: String) = grantedPermissions.contains(permission)

    /**
     * Returns true if specific permission is denied
     *
     * @param permission - permission to check
     * */
    fun isDenied(permission: String) = deniedPermissions.contains(permission)

    /**
     * Returns true if specific permission needs explanation
     *
     * @param permission - permission to check
     * */
    fun isShouldShowRequestPermissionRationale(permission: String): Boolean = event?.isShouldShowRequestPermissionRationale(permission) ?: false

    override fun toString(): String {
        return "Permissions: Granted: ${grantedPermissions.joinToString()}; Denied: ${deniedPermissions.joinToString()}"
    }
}
