package com.app.productcatalog.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.app.productcatalog.data.model.ProductDto

@Dao
interface ProductsDao {

    @Query(
        """
    SELECT * FROM product 
    WHERE (:query = '' OR name LIKE '%' || :query || '%') 
    AND (:isFavourite = -1 OR isFavourite = :isFavourite)
        """
    )
    suspend fun getProducts(isFavourite: Int, query: String?): List<ProductDto>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAllProduct(vararg product: ProductDto)

    @Update
    suspend fun updateProduct(product: ProductDto)
}