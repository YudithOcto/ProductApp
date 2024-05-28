package com.app.productcatalog.domain.repository

import com.app.productcatalog.data.local.ProductsDao
import com.app.productcatalog.data.model.ProductDto
import com.app.productcatalog.domain.mapper.ProductMapper
import com.app.productcatalog.domain.model.ProductSpec
import com.app.productcatalog.util.Result
import com.app.productcatalog.util.SchedulerProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

interface ProductRepository {
    suspend fun getProducts(isFavourite: Int, query: String?): Flow<Result<List<ProductSpec>>>
    suspend fun insertProducts(): Flow<Result<Unit>>
    suspend fun updateProductFavourite(productSpec: ProductSpec): Flow<Result<Boolean>>
}

class DefaultProductRepository @Inject constructor(
    private val productDao: ProductsDao,
    private val productMapper: ProductMapper,
    private val schedulerProvider: SchedulerProvider,
    private val productList: List<ProductDto>
) : ProductRepository {

    override suspend fun getProducts(
        isFavourite: Int,
        query: String?
    ): Flow<Result<List<ProductSpec>>> {
        return flow {
            try {
                val products = productDao.getProducts(isFavourite, query)
                val productsSpec = products.map { productMapper.convertToProductSpec(it) }
                emit(Result.Success(productsSpec))
            } catch (e: Exception) {
                emit(Result.Error(e))
            }

        }.flowOn(schedulerProvider.default())
    }

    override suspend fun insertProducts(): Flow<Result<Unit>> {
       return flow {
           try {
               productDao.insertAllProduct(*productList.toTypedArray())
               emit(Result.Success(Unit))
           } catch (e: Exception) {
               emit(Result.Error(e))
           }
       }.flowOn(schedulerProvider.default())
    }

    override suspend fun updateProductFavourite(productSpec: ProductSpec): Flow<Result<Boolean>> {
        return flow {
            try {
                productDao.updateProduct(productMapper.convertToProductDto(productSpec))
                emit(Result.Success(true))
            } catch (e: Exception) {
                emit(Result.Error(e))
            }
        }.flowOn(schedulerProvider.default())
    }
}