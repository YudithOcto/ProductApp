package com.app.productcatalog.mockdata

import com.app.productcatalog.data.model.ProductDto
import com.app.productcatalog.domain.model.ProductSpec

object MockData {
    val productDtoList: List<ProductDto>
        get() = listOf(
            ProductDto(
                id = "1",
                name = "Smartphone",
                description = "Latest Android Smartphone",
                price = 699.99,
                isFavourite = false,
                imageUrl = "https://www.static-src.com/wcsstore/Indraprastha/images/catalog/full//105/MTA-44094578/samsung_samsung_galaxy_s21_garansi_resmi_full02_s49mg6ar.jpg"
            ),
            ProductDto(
                id = "2",
                name = "Laptop",
                description = "Powerful gaming laptop",
                price = 1299.99,
                isFavourite = false,
                imageUrl = "https://images-cdn.ubuy.co.id/650188aee584193d8350a66b-2022-apple-macbook-pro-laptop-with-m2.jpg"
            ),
            ProductDto(
                id = "3",
                name = "Headphones",
                description = "Noise-cancelling headphones",
                price = 199.99,
                isFavourite = false,
                imageUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSou_80YdnXtU6p4bWke0Gmkz5MRf_v2vRIRyN_raBYOw&s"
            ),
            ProductDto(
                id = "4",
                name = "Smartwatch",
                description = "Feature-rich smartwatch",
                price = 299.99,
                isFavourite = false,
                imageUrl = "https://images-cdn.ubuy.co.id/633b12488d2edc65997f4c20-smart-watch-bluetooth-smartwatch-touch.jpg"
            ),
            ProductDto(
                id = "5",
                name = "Camera",
                description = "High-resolution digital camera",
                price = 499.99,
                isFavourite = false,
                imageUrl = "https://id.canon/media/image/2023/05/19/b89bed4e21e540f985dedebe80166def_EOS+R100+RF-S18-45mm+Front+Slant.png"
            ),
            ProductDto(
                id = "6",
                name = "Tablet",
                description = "Lightweight and powerful tablet",
                price = 399.99,
                isFavourite = false,
                imageUrl = "https://consumer.huawei.com/content/dam/huawei-cbg-site/common/mkt/plp/tablet-new/matepad-pro-series-card-1.jpg"
            ),
            ProductDto(
                id = "7",
                name = "Wireless Mouse",
                description = "Ergonomic wireless mouse",
                price = 29.99,
                isFavourite = false,
                imageUrl = "https://images-cdn.ubuy.co.id/633b808a915dbd36797690cb-gaming-mouse-redragon-wireless-mouse.jpg"
            ),
            ProductDto(
                id = "8",
                name = "Keyboard",
                description = "Mechanical keyboard",
                price = 89.99,
                isFavourite = false,
                imageUrl = "https://id-test-11.slatic.net/p/11c37049e7450163265c51c44ae4acf6.jpg"
            ),
            ProductDto(
                id = "9",
                name = "Monitor",
                description = "4K Ultra HD monitor",
                price = 499.99,
                isFavourite = false,
                imageUrl = "https://m.media-amazon.com/images/I/81FFCjMtVZL.jpg"
            ),
            ProductDto(
                id = "10",
                name = "External Hard Drive",
                description = "2TB external hard drive",
                price = 99.99,
                isFavourite = false,
                imageUrl = "https://www.sweetwater.com/sweetcare/media/2019/04/External-Hard-Drive-Hero.png"
            )
        )

    val productSpecList: List<ProductSpec>
        get() = listOf(
            ProductSpec(
                id = "1",
                name = "Smartphone",
                description = "Latest Android Smartphone",
                price = 699.99,
                isFavourite = false,
                imageUrl = "https://www.static-src.com/wcsstore/Indraprastha/images/catalog/full//105/MTA-44094578/samsung_samsung_galaxy_s21_garansi_resmi_full02_s49mg6ar.jpg"
            ),
            ProductSpec(
                id = "2",
                name = "Laptop",
                description = "Powerful gaming laptop",
                price = 1299.99,
                isFavourite = false,
                imageUrl = "https://images-cdn.ubuy.co.id/650188aee584193d8350a66b-2022-apple-macbook-pro-laptop-with-m2.jpg"
            ),
            ProductSpec(
                id = "3",
                name = "Headphones",
                description = "Noise-cancelling headphones",
                price = 199.99,
                isFavourite = false,
                imageUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSou_80YdnXtU6p4bWke0Gmkz5MRf_v2vRIRyN_raBYOw&s"
            ),
            ProductSpec(
                id = "4",
                name = "Smartwatch",
                description = "Feature-rich smartwatch",
                price = 299.99,
                isFavourite = false,
                imageUrl = "https://images-cdn.ubuy.co.id/633b12488d2edc65997f4c20-smart-watch-bluetooth-smartwatch-touch.jpg"
            ),
            ProductSpec(
                id = "5",
                name = "Camera",
                description = "High-resolution digital camera",
                price = 499.99,
                isFavourite = false,
                imageUrl = "https://id.canon/media/image/2023/05/19/b89bed4e21e540f985dedebe80166def_EOS+R100+RF-S18-45mm+Front+Slant.png"
            ),
            ProductSpec(
                id = "6",
                name = "Tablet",
                description = "Lightweight and powerful tablet",
                price = 399.99,
                isFavourite = false,
                imageUrl = "https://consumer.huawei.com/content/dam/huawei-cbg-site/common/mkt/plp/tablet-new/matepad-pro-series-card-1.jpg"
            ),
            ProductSpec(
                id = "7",
                name = "Wireless Mouse",
                description = "Ergonomic wireless mouse",
                price = 29.99,
                isFavourite = false,
                imageUrl = "https://images-cdn.ubuy.co.id/633b808a915dbd36797690cb-gaming-mouse-redragon-wireless-mouse.jpg"
            ),
            ProductSpec(
                id = "8",
                name = "Keyboard",
                description = "Mechanical keyboard",
                price = 89.99,
                isFavourite = false,
                imageUrl = "https://id-test-11.slatic.net/p/11c37049e7450163265c51c44ae4acf6.jpg"
            ),
            ProductSpec(
                id = "9",
                name = "Monitor",
                description = "4K Ultra HD monitor",
                price = 499.99,
                isFavourite = false,
                imageUrl = "https://m.media-amazon.com/images/I/81FFCjMtVZL.jpg"
            ),
            ProductSpec(
                id = "10",
                name = "External Hard Drive",
                description = "2TB external hard drive",
                price = 99.99,
                isFavourite = false,
                imageUrl = "https://www.sweetwater.com/sweetcare/media/2019/04/External-Hard-Drive-Hero.png"
            )
        )
}