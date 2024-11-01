package com.example.shopmobileapplication.data.bucket

import android.content.Context
import com.example.shopmobileapplication.data.Bucket
import com.example.shopmobileapplication.data.Product
import com.example.shopmobileapplication.data.User
import com.example.shopmobileapplication.data.product.ProductRepositoryImpl
import com.example.shopmobileapplication.utils.NumericException
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest

class BucketRepositoryImpl(
    private val context: Context,
    private val supabaseClient: SupabaseClient
): BucketRepository {
    override suspend fun getBucketList(user: User): Result<List<Bucket>> = try {
        val buckets = supabaseClient.postgrest["buckets"].select(filter = {
            Bucket::userId eq user.id
        }).decodeList<Bucket>()
        Result.success(buckets)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun getOneBucket(user: User, product: Product): Result<Bucket> = try {
        val bucket = supabaseClient.postgrest["buckets"].select(filter = {
            and {
                Bucket::userId eq user.id
                Bucket::productId eq product.id
            }
        }).decodeSingle<Bucket>()
        Result.success(bucket)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun addProductToBucket(bucket: Bucket): Result<Boolean> = try {
        supabaseClient.postgrest["buckets"].insert(bucket)
        Result.success(true)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun updateBucket(bucket: Bucket): Result<Boolean> = try {
        supabaseClient.postgrest["buckets"].update(
            {
                Bucket::quantity setTo bucket.quantity
            }
        ) {
            and {
                Bucket::userId eq bucket.userId
                Bucket::productId eq bucket.productId
            }
        }
        Result.success(true)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun deleteBucket(bucket: Bucket): Result<Boolean> = try {
        supabaseClient.postgrest["buckets"].delete(filter = {
            and {
                Bucket::productId eq bucket.productId
                Bucket::userId eq bucket.userId
            }
        })
        Result.success(true)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun getBucketSum(bucketList: List<Bucket>): Result<Double> = try {
        var resSum = 0.0
        bucketList.forEach { order ->
            val product = ProductRepositoryImpl.getOneProductById(order.productId, supabaseClient)
            if (product == null) {
                resSum = -1.0
                return@forEach
            }
            resSum += product.price * order.quantity
        }
        if (resSum < 0) {
            throw NumericException.LessZeroException
        }
        Result.success(resSum)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun getProductsInBucket(user: User): Result<List<Product>> = try {
        val favorites = supabaseClient.postgrest["buckets"].select(filter = {
            Bucket::userId eq user.id
        }).decodeList<Bucket>()
        val products = mutableListOf<Product>()
        favorites.forEach { fav ->
            ProductRepositoryImpl.getOneProductById(fav.productId, supabaseClient)
                ?.let { products.add(it) }
        }
        Result.success(products.toList())
    } catch (e: Exception) {
        Result.failure(e)
    }
}