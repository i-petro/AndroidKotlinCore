package com.androidkotlincore.mvp.impl.permissions

/**
 * Created by Peter on 22.01.18.
 */
class PermissionException(message: String, val result: RequestPermissionsResult) : IllegalStateException(message)