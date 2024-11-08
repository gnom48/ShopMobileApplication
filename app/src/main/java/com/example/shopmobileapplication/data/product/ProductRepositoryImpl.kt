package com.example.shopmobileapplication.data.product

import android.content.Context
import com.example.shopmobileapplication.data.Bucket
import com.example.shopmobileapplication.data.Favorite
import com.example.shopmobileapplication.data.Product
import com.example.shopmobileapplication.data.ProductCategory
import com.example.shopmobileapplication.data.ProductSize
import com.example.shopmobileapplication.data.User
import com.example.shopmobileapplication.ui.main.search.ProductFilter
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest

class ProductRepositoryImpl(
    private val context: Context?,
    private val supabaseClient: SupabaseClient
): ProductRepository {
    companion object {
        suspend fun getOneProductById(id: String, supabaseClient: SupabaseClient): Product? = try {
            supabaseClient.postgrest[Product.tableName].select(filter = {
                Product::id eq id
            }).decodeSingle<Product>()
        } catch (e: Exception) {
            null
        }

        suspend fun getOneProductSizeById(id: Int, supabaseClient: SupabaseClient): ProductSize? = try {
            supabaseClient.postgrest[ProductSize.tableName].select(filter = {
                ProductSize::id eq id
            }).decodeSingle<ProductSize>()
        } catch (e: Exception) {
            null
        }

        suspend fun getProductByProductSize(productSizeId: Int, supabaseClient: SupabaseClient): Product? = try {
            val productSize = supabaseClient.postgrest[ProductSize.tableName].select(filter = {
                ProductSize::id eq productSizeId
            }).decodeSingle<ProductSize>()
            val product = supabaseClient.postgrest[Product.tableName].select(filter = {
                Product::id eq productSize.productId
            }).decodeSingle<Product>()
            product
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun getProductById(id: String): Result<Product> = try {
        val product = supabaseClient.postgrest[Product.tableName].select(filter = {
            Product::id eq id
        }).decodeList<Product>().first()
        Result.success(product)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun getAllProducts(limit: Int?): Result<List<Product>> = try {
        val products = supabaseClient.postgrest[Product.tableName].select().decodeList<Product>()
        if (limit != null) {
            Result.success(products.takeLast(limit))
        } else {
            Result.success(products)
        }
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun getSearchResultByProductName(query: String): Result<List<Product>> = try {
        val allProducts = supabaseClient.postgrest[Product.tableName].select(filter = {
            Product::name ilike "%${query}%"
        }).decodeList<Product>()
        Result.success(allProducts)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun getSearchResultByFilters(filter: ProductFilter): Result<List<Product>> = try {
        val productSizes = supabaseClient.postgrest[Product.tableName].select(filter = {
            and {
                filter.minPrice?.let { p -> Product::price gte p }
                filter.maxPrice?.let { p -> Product::price lte p }
                // FIXME: ааа здесь какой-то ад
                filter.sizes?.let { sizes ->
                    Product::id contained supabaseClient.postgrest[ProductSize.tableName].select(filter = {
                        ProductSize::sizeRus contained sizes
                    }).decodeList<ProductSize>().map { it.productId }
                }
                filter.stores?.let { stores ->
                    Product::id contained supabaseClient.postgrest[ProductSize.tableName].select(filter = {
                        ProductSize::storeId contained stores
                    }).decodeList<ProductSize>().map { it.productId }
                }
            }
        }).decodeList<Product>()
        val uniqueProductIds = productSizes.map { it.id }
        var products = mutableListOf<Product>()
        uniqueProductIds.forEach { id ->
            ProductRepositoryImpl.getOneProductById(id, supabaseClient)?.let { products.add(it) }
        }
        Result.success(products)
    } catch (e: Exception) {
        Result.failure(e)
    }

//    override suspend fun getSearchResultByFilters(filter: ProductFilter): Result<List<Product>> {
//        val sqlQuery = """
//            SELECT p.id, p.name, p.category, p.description, p.price, p.image, p.seller_id
//            FROM products p
//            JOIN products_sizes ps ON p.id = ps.product_id
//            WHERE (ps.size_rus IN (:sizes) OR (:sizes) IS NULL)
//              AND (ps.store_id IN (:stores) OR (:stores) IS NULL)
//              AND (p.price >= :minPrice OR :minPrice IS NULL)
//              AND (p.price <= :maxPrice OR :maxPrice IS NULL);
//        """
//        val response = supabaseClient.postgrest.rpc("execute_sql", mapOf(
//            "query" to sqlQuery,
//            "sizes" to filter.sizes,
//            "stores" to filter.stores,
//            "minPrice" to filter.minPrice,
//            "maxPrice" to filter.maxPrice
//        ))
//        val res = response.decodeList<Product>()
//        return Result.success(res)
//    }

    override suspend fun getProductsByCategory(category: ProductCategory): Result<List<Product>> = try {
        val products = supabaseClient.postgrest[Product.tableName].select(filter = {
            Product::category eq category.id
        }).decodeList<Product>()
        Result.success(products)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun getProductsInBucket(user: User): Result<List<Product>> = try {
        val buckets = supabaseClient.postgrest[Bucket.tableName].select(filter = {
            Bucket::userId eq user.id
        }).decodeList<Bucket>()
        val products = mutableListOf<Product>()
        buckets.forEach {
            getOneProductById(ProductRepositoryImpl.getProductByProductSize(it.productExampleId!!, supabaseClient)!!.id, supabaseClient)?.let { it1 -> products.add(it1) }
        }
        Result.success(products)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun getProductsInFavorite(user: User): Result<List<Product>> = try {
        val favorites = supabaseClient.postgrest[Favorite.tableName].select(filter = {
            Favorite::userId eq user.id
        }).decodeList<Favorite>()
        val products = mutableListOf<Product>()
        favorites.forEach {
            getOneProductById(it.productId, supabaseClient)?.let { it1 -> products.add(it1) }
        }
        Result.success(products)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun getProductsCategories(): Result<List<ProductCategory>> = try {
        val categories = supabaseClient.postgrest[ProductCategory.tableName].select().decodeList<ProductCategory>()
        Result.success(categories)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun getProductSizes(productId: String): Result<List<ProductSize>> = try {
        val products = supabaseClient.postgrest[ProductSize.tableName].select(filter = {
            ProductSize::productId eq productId
        }).decodeList<ProductSize>()
        Result.success(products)
    } catch (e: Exception) {
        Result.failure(e)
    }
}