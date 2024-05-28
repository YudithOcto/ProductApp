package com.app.productcatalog.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "product")
data class ProductDto(
    @PrimaryKey val id: String,
    @ColumnInfo val name: String?,
    @ColumnInfo val description: String?,
    @ColumnInfo val price: Double?,
    @ColumnInfo val isFavourite: Boolean?,
    @ColumnInfo val imageUrl: String?
)
