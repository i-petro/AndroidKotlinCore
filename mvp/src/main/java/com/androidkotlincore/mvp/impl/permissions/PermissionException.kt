package com.androidkotlincore.mvp.impl.permissions

/**
 * Created by Peter on 22.01.18.
 */
/**
 * Custom permission exception.
 *
 * @param message - exception text
 * @param result - [RequestPermissionsResult], container with information permissions response
 * */
class PermissionException(message: String, val result: RequestPermissionsResult) : IllegalStateException(message)