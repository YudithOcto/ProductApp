package com.app.productcatalog.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.app.productcatalog.data.model.ProductDto

@Database(entities = [ProductDto::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun productDao(): ProductsDao
}