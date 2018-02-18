package com.androidkotlincore.mvp.addons

import java.util.concurrent.atomic.AtomicInteger

/**
 * Created by Peter on 18.02.2018.
 */
private val requestCodeGenerator = AtomicInteger(90)

fun generateRequestCode() = requestCodeGenerator.getAndIncrement()