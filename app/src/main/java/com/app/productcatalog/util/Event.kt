package com.app.productcatalog.util

import java.util.concurrent.atomic.AtomicBoolean


class Event<T>(private val data: T) {
    private var consumed = AtomicBoolean(false)
    fun consumeOnce(consumer: (T) -> Unit) {
        if (consumed.compareAndSet(false, true)) {
            consumer(data)
        }
    }
}