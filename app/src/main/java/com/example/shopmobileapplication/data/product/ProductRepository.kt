package com.example.shopmobileapplication.data.product

import com.example.shopmobileapplication.data.Product
import com.example.shopmobileapplication.data.ProductCategory
import com.example.shopmobileapplication.data.ProductSize
import com.example.shopmobileapplication.data.User
import com.example.shopmobileapplication.ui.main.search.ProductFilter
import com.example.shopmobileapplication.ui.viewmodel.BaseRepository

interface ProductRepository: BaseRepository {
    suspend fun getProductById(id: String): Result<Product>

    suspend fun getAllProducts(limit: Int? = null): Result<List<Product>>

    suspend fun getSearchResultByProductName(query: String): Result<List<Product>>

    suspend fun getProductsByCategory(category: ProductCategory): Result<List<Product>>

    suspend fun getProductsInBucket(user: User): Result<List<Product>>

    suspend fun getProductsInFavorite(user: User): Result<List<Product>>

    suspend fun getProductsCategories(): Result<List<ProductCategory>>

    suspend fun getProductSizes(productId: String): Result<List<ProductSize>>

    suspend fun getSearchResultByFilters(filter: ProductFilter): Result<List<Product>>
}