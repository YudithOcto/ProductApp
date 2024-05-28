package com.app.productcatalog.util

import kotlinx.coroutines.CoroutineDispatcher

interface SchedulerProvider {

    fun io(): CoroutineDispatcher

    fun ui(): CoroutineDispatcher

    fun default(): CoroutineDispatcher

    fun unconfined(): CoroutineDispatcher
}
