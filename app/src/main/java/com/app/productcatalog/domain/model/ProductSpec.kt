package com.app.productcatalog.domain.model

import androidx.compose.runtime.Stable

@Stable
data class ProductSpec(
    val id: String,
    val name: String,
    val description: String,
    val price: Double,
    val isFavourite: Boolean,
    val imageUrl: String,
)