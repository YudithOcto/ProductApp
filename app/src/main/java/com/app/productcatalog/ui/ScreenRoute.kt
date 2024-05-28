package com.app.productcatalog.ui

sealed class ScreenRoute(val name: String) {
    object ProductListScreen: ScreenRoute("ProductListScreen")
}