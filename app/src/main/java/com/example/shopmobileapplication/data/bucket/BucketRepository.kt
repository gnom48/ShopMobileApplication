package com.example.shopmobileapplication.data.bucket


import com.example.shopmobileapplication.data.Bucket
import com.example.shopmobileapplication.data.Product
import com.example.shopmobileapplication.data.User
import com.example.shopmobileapplication.viewmodel.BaseRepository

interface BucketRepository: BaseRepository {
    suspend fun getBucketList(user: User): Result<List<Bucket>>

    suspend fun getOneBucket(user: User, product: Product): Result<Bucket>

    suspend fun addProductToBucket(bucket: Bucket): Result<Boolean>

    suspend fun updateBucket(bucket: Bucket): Result<Boolean>

    suspend fun deleteBucket(bucket: Bucket): Result<Boolean>

    suspend fun getBucketSum(bucketList: List<Bucket>): Result<Double>

    suspend fun getProductsInBucket(user: User): Result<List<Product>>
}