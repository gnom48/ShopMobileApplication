package com.example.shopmobileapplication.data.bucket

import android.content.Context
import com.example.shopmobileapplication.data.Bucket
import com.example.shopmobileapplication.data.Product
import com.example.shopmobileapplication.data.ProductSize
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
        val buckets = supabaseClient.postgrest[Bucket.tableName].select() {
            filter {
                Bucket::userId eq user.id
            }
        }.decodeList<Bucket>()
        Result.success(buckets)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun getOneBucket(user: User, product: Product): Result<Bucket> = try {
        val bucket = supabaseClient.postgrest[Bucket.tableName].select() {
            filter {
                and {
                    Bucket::userId eq user.id
                    Bucket::productExampleId eq product.id
                }
            }
        }.decodeSingle<Bucket>()
        Result.success(bucket)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun addProductToBucket(bucket: Bucket): Result<Boolean> = try {
        supabaseClient.postgrest[Bucket.tableName].insert(bucket)
        Result.success(true)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun updateBucket(bucket: Bucket): Result<Boolean> = try {
        supabaseClient.postgrest[Bucket.tableName].update(
            {
                Bucket::quantity setTo bucket.quantity
            }
        ) {
            filter {
                and {
                    Bucket::userId eq bucket.userId
                    Bucket::productExampleId eq bucket.productExampleId
                }
            }
        }
        Result.success(true)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun deleteBucket(bucket: Bucket): Result<Boolean> = try {
        supabaseClient.postgrest[Bucket.tableName].delete {
            filter {
                and {
                    Bucket::productExampleId eq bucket.productExampleId
                    Bucket::userId eq bucket.userId
                }
            }
        }
        Result.success(true)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun getBucketSum(bucketList: List<Bucket>): Result<Double> = try {
        var resSum = 0.0
        bucketList.forEach { b ->
            val product = ProductRepositoryImpl.getOneProductById(ProductRepositoryImpl.getProductByProductSize(b.productExampleId!!, supabaseClient)!!.id, supabaseClient)
            if (product == null) {
                resSum = -1.0
                return@forEach
            }
            resSum += product.price * b.quantity
        }
        if (resSum < 0) {
            throw NumericException.LessZeroException
        }
        Result.success(resSum)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun getProductsInBucket(user: User): Result<List<Product>> = try {
        val buckets = supabaseClient.postgrest[Bucket.tableName].select() {
            filter {
                Bucket::userId eq user.id
            }
        }.decodeList<Bucket>()
        val products = mutableListOf<Product>()
        buckets.forEach { b ->
            ProductRepositoryImpl.getOneProductById(ProductRepositoryImpl.getProductByProductSize(b.productExampleId!!, supabaseClient)!!.id, supabaseClient)
                ?.let { products.add(it) }
        }
        Result.success(products.toList())
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun getProductsSizesInBucket(user: User): Result<List<ProductSize>> = try {
        val buckets = supabaseClient.postgrest[Bucket.tableName].select() {
            filter {
                Bucket::userId eq user.id
            }
        }.decodeList<Bucket>()
        val products = mutableListOf<ProductSize>()
        buckets.forEach { b ->
            ProductRepositoryImpl.getOneProductSizeById(b.productExampleId!!, supabaseClient)
                ?.let { products.add(it) }
        }
        Result.success(products)
    } catch (e: Exception) {
        Result.failure(e)
    }
}