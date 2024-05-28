package com.app.productcatalog.domain.mapper

import com.app.productcatalog.data.model.ProductDto
import com.app.productcatalog.domain.model.ProductSpec

interface ProductMapper {
    fun convertToProductSpec(data: ProductDto): ProductSpec
    fun convertToProductDto(data: ProductSpec): ProductDto
}

class DefaultProductMapper : ProductMapper {
    override fun convertToProductSpec(data: ProductDto): ProductSpec {
        return ProductSpec(
            id = data.id,
            name = data.name.orEmpty(),
            description = data.description.orEmpty(),
            price = data.price ?: 0.0,
            imageUrl = data.imageUrl.orEmpty(),
            isFavourite = data.isFavourite ?: false,
        )
    }

    override fun convertToProductDto(data: ProductSpec): ProductDto {
        return ProductDto(
            id = data.id,
            name = data.name,
            description = data.description,
            price = data.price,
            imageUrl = data.imageUrl,
            isFavourite = data.isFavourite,
        )
    }

}