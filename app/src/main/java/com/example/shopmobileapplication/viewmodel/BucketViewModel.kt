package com.example.shopmobileapplication.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.shopmobileapplication.data.Bucket
import com.example.shopmobileapplication.data.Product
import com.example.shopmobileapplication.data.ProductSize
import com.example.shopmobileapplication.data.User
import com.example.shopmobileapplication.data.bucket.BucketRepository
import kotlinx.coroutines.launch

class BucketViewModelFactory(private val bucketRepository: BucketRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BucketViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BucketViewModel(bucketRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class BucketViewModel(
    private val bucketRepository: BucketRepository
) : BaseViewModel() {
    private val _buckets = mutableStateOf<List<Bucket>>(emptyList())
    val buckets by _buckets

    suspend fun getBucketList(user: User) {
        viewModelScope.launch {
            withLoading {
                bucketRepository.getBucketList(user).onSuccess {
                    _buckets.value = it
                    _error.value = null
                    getBucketSum()
                }.onFailure { e ->
                    _error.value = e
                    _buckets.value = emptyList()
                }
            }
        }
    }

    suspend fun addProductToBucket(bucket: Bucket) {
        viewModelScope.launch {
            withLoading {
                bucketRepository.addProductToBucket(bucket).onSuccess {
                    _buckets.value.toMutableList().add(bucket)
                    _error.value = null
                    getBucketSum()
                }.onFailure { e ->
                    _error.value = e
                }
            }

        }
    }

    suspend fun updateBucket(bucket: Bucket) {
        viewModelScope.launch {
            withLoading {
                bucketRepository.updateBucket(bucket).onSuccess {
                    _error.value = null
                    _buckets.value = _buckets.value.map { b ->
                        if (b.productExampleId == bucket.productExampleId && b.userId == bucket.userId) {
                            b.quantity = bucket.quantity; b
                        } else {
                            b
                        }
                    }
                    getBucketSum()
                }.onFailure { e ->
                    _error.value = e
                }
            }
        }
    }

    suspend fun deleteBucket(bucket: Bucket) {
        viewModelScope.launch {
            withLoading {
                bucketRepository.deleteBucket(bucket).onSuccess {
                    _error.value = null
                    _buckets.value.toMutableList().remove(bucket)
                    getBucketList(UserViewModel.currentUser)
                    getProductsInBucket(UserViewModel.currentUser)
                    getProductsSizesInBucket(UserViewModel.currentUser)
                    getBucketSum()
                }.onFailure { e ->
                    _error.value = e
                }
            }

        }
    }

    private val _bucketSum = mutableDoubleStateOf(0.0)
    val bucketSum by _bucketSum

    suspend fun getBucketSum() {
        viewModelScope.launch {
            withLoading {
                if (_buckets.value.isEmpty()) {
                    getBucketList(UserViewModel.currentUser)
                    return@withLoading
                }
                bucketRepository.getBucketSum(_buckets.value).onSuccess {
                    _bucketSum.doubleValue = it
                    _error.value = null
                }.onFailure { e ->
                    _error.value = e
                    _bucketSum.doubleValue = 0.0
                }
            }
        }
    }

    private val _productsInBucket = mutableStateOf<List<Product>>(emptyList())
    val productsInBucket by _productsInBucket

    suspend fun getProductsInBucket(user: User) {
        viewModelScope.launch {
            withLoading {
                bucketRepository.getProductsInBucket(user).onSuccess {
                    _productsInBucket.value = it
                    _error.value = null
                }.onFailure { e ->
                    _error.value = e
                    _productsInBucket.value = emptyList()
                }
            }
        }
    }

    private val _productsSizesInBucket = mutableStateOf<List<ProductSize>>(emptyList())
    val productsSizesInBucket by _productsSizesInBucket

    suspend fun getProductsSizesInBucket(user: User) {
        viewModelScope.launch {
            withLoading {
                bucketRepository.getProductsSizesInBucket(user).onSuccess {
                    _productsSizesInBucket.value = it
                    _error.value = null
                }.onFailure { e ->
                    _error.value = e
                    _productsSizesInBucket.value = emptyList()
                }
            }
        }
    }
}