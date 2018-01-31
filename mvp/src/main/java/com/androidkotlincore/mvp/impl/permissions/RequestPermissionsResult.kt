package com.androidkotlincore.mvp.impl.permissions

/**
 * Created by Peter on 19.01.18.
 */
class RequestPermissionsResult {
    val grantedPermissions: List<String>
    val deniedPermissions: List<String>
    private val event: OnRequestPermissionsResultEvent?

    constructor(grantedPermissions: List<String>, deniedPermissions: List<String>) {
        this.grantedPermissions = grantedPermissions
        this.deniedPermissions = deniedPermissions
        this.event = null
    }

    constructor(event: OnRequestPermissionsResultEvent) {
        this.event = event
        this.grantedPermissions = event.permissions.filter { event.isGranted(it) }
        this.deniedPermissions = event.permissions.filter { !event.isGranted(it) }
    }

    val isAllGranted get() = deniedPermissions.isEmpty()
    fun isGranted(permission: String) = grantedPermissions.contains(permission)
    fun isDenied(permission: String) = deniedPermissions.contains(permission)
    fun isShouldShowRequestPermissionRationale(permission: String): Boolean = event?.isShouldShowRequestPermissionRationale(permission) ?: false

    override fun toString(): String {
        return "Permissions: Granted: ${grantedPermissions.joinToString()}; Denied: ${deniedPermissions.joinToString()}"
    }
}
