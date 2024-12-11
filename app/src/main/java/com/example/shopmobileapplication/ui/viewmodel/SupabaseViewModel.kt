package com.example.shopmobileapplication.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.example.shopmobileapplication.data.DataState
import com.example.shopmobileapplication.data.network.SupabaseClient
import io.github.jan.supabase.exceptions.RestException
import io.github.jan.supabase.storage.BucketApi
import io.github.jan.supabase.storage.storage
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.minutes

class SupabaseViewModel: BaseViewModel() {
    private var _imgState = mutableStateOf<DataState>(DataState.Nothing)
    val imgState by _imgState

    fun dismissSuccess() {
        _imgState.value = DataState.Nothing
    }

    private val supabaseClient = SupabaseClient.client

    private suspend fun createPrivateBucketInNotExists(bucketName: String): BucketApi? = withLoading {
            try {
                _error.value = null
                val b = supabaseClient.storage.retrieveBucketById(bucketName)!!
                supabaseClient.storage[bucketName]
            } catch (e: RestException) {
                supabaseClient.storage.createBucket(bucketName) {
                    public = false
                    fileSizeLimit = 10.megabytes
                }
                _error.value = null
                supabaseClient.storage[bucketName]!!
            } catch (e: Exception) {
                _error.value = e
                null
            }
        }

    fun uploadFileToPrivateBucket(userId: String = UserViewModel.currentUser.id, fileName: String, byteArray: ByteArray) {
        val bucketName = "images-$userId"
        viewModelScope.launch {
            withLoading {
                if (byteArray.isEmpty() || fileName.isNullOrEmpty()) {
                    return@withLoading
                }
                try {
                    val bucket = createPrivateBucketInNotExists(bucketName)
                    bucket!!.upload(fileName, byteArray, true)
                    _error.value = null
                    _imgState.value = DataState.Success()
                } catch (e: Exception) {
                    _error.value = e
                }
            }
        }
    }

    fun getUrlFromPublicBucketCallback(bucketName: String = "images", fileName: String, onImageUrlRetriever: (url: String?) -> Unit) {
        viewModelScope.launch {
            withLoading {
                try {
                    val bucket = SupabaseClient.client.storage[bucketName]
                    val url = bucket.publicUrl(fileName)
                    onImageUrlRetriever(url)
                    _error.value = null
                } catch (e: Exception) {
                    onImageUrlRetriever(null)
                    _error.value = e
                }
            }
        }
    }

    fun getSignedUrlFromPrivateBucket(userId: String, fileName: String, onImageUrlRetriever: (url: String?) -> Unit) {
        val bucketName = "images-$userId"
        viewModelScope.launch {
            withLoading {
                try {
                    val bucket = SupabaseClient.client.storage[bucketName]
                    val url = bucket.createSignedUrl(fileName, 10.minutes)
                    _error.value = null
                    onImageUrlRetriever("${SupabaseClient.supabaseBaseUrl}/storage/v1/$url")
                } catch (e: Exception) {
                    _error.value = e
                    onImageUrlRetriever(null)
                }
            }
        }
    }

    suspend fun getUrlFromPublicBucket(bucketName: String, fileName: String): String? = withLoading {
            try {
                val bucket = SupabaseClient.client.storage[bucketName]
                val url = bucket.publicUrl(fileName)
                _error.value = null
                url
            } catch (e: Exception) {
                _error.value = e
                null
            }
        }
}
