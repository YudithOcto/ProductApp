package com.app.productcatalog.scheduler

import com.app.productcatalog.util.SchedulerProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.test.StandardTestDispatcher

class TaskSchedulerProvider constructor(
    val testDispatcher: CoroutineDispatcher = StandardTestDispatcher()
): SchedulerProvider {
    override fun io(): CoroutineDispatcher = testDispatcher

    override fun ui(): CoroutineDispatcher = testDispatcher

    override fun default(): CoroutineDispatcher = testDispatcher

    override fun unconfined(): CoroutineDispatcher = testDispatcher

}