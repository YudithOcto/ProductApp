package com.app.productcatalog.util

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class AppSchedulerProvider: SchedulerProvider {
    override fun io(): CoroutineDispatcher = Dispatchers.IO
    override fun ui(): CoroutineDispatcher = Dispatchers.Main
    override fun default(): CoroutineDispatcher = Dispatchers.Default
    override fun unconfined(): CoroutineDispatcher = Dispatchers.Unconfined

}