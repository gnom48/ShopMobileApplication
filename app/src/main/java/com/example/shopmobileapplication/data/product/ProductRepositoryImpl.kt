package com.example.shopmobileapplication.data.product

import android.content.Context
import com.example.shopmobileapplication.data.Bucket
import com.example.shopmobileapplication.data.Favorite
import com.example.shopmobileapplication.data.Product
import com.example.shopmobileapplication.data.ProductCategory
import com.example.shopmobileapplication.data.User
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest

class ProductRepositoryImpl(
    private val context: Context,
    private val supabaseClient: SupabaseClient
): ProductRepository {
    companion object {
        suspend fun getOneProductById(id: String, supabaseClient: SupabaseClient): Product? = try {
            supabaseClient.postgrest["products"].select(filter = {
                Product::id eq id
            }).decodeList<Product>().first()
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun getProductById(id: String): Result<Product> = try {
        val product = supabaseClient.postgrest["products"].select(filter = {
            Product::id eq id
        }).decodeList<Product>().first()
        Result.success(product)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun getAllProducts(limit: Int?): Result<List<Product>> = try {
        val products = supabaseClient.postgrest["products"].select().decodeList<Product>()
        if (limit != null) {
            Result.success(products.takeLast(limit))
        } else {
            Result.success(products)
        }
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun getSearchResultByProductName(query: String): Result<List<Product>> = try {
        val allProducts = supabaseClient.postgrest["products"].select(filter = {
            Product::name ilike query
        }).decodeList<Product>()
        Result.success(allProducts)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun getProductsByCategory(category: ProductCategory): Result<List<Product>> = try {
        val products = supabaseClient.postgrest["products"].select(filter = {
            Product::category eq category.id
        }).decodeList<Product>()
        Result.success(products)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun getProductsInBucket(user: User): Result<List<Product>> = try {
        val buckets = supabaseClient.postgrest["buckets"].select(filter = {
            Bucket::userId eq user.id
        }).decodeList<Bucket>()
        val products = mutableListOf<Product>()
        buckets.forEach {
            getOneProductById(it.productId, supabaseClient)?.let { it1 -> products.add(it1) }
        }
        Result.success(products)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun getProductsInFavorite(user: User): Result<List<Product>> = try {
        val buckets = supabaseClient.postgrest["favorite"].select(filter = {
            Favorite::userId eq user.id
        }).decodeList<Bucket>()
        val products = mutableListOf<Product>()
        buckets.forEach {
            getOneProductById(it.productId, supabaseClient)?.let { it1 -> products.add(it1) }
        }
        Result.success(products)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun getProductsCategories(): Result<List<ProductCategory>> = try {
        val categories = supabaseClient.postgrest["categories"].select().decodeList<ProductCategory>()
        Result.success(categories)
    } catch (e: Exception) {
        Result.failure(e)
    }
}