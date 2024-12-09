package com.example.shopmobileapplication.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopmobileapplication.data.DataState
import com.example.shopmobileapplication.data.network.SupabaseClient
import io.github.jan.supabase.storage.storage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SupabaseViewModel: ViewModel() {
    private var _dataState = MutableStateFlow<DataState>(DataState.Loading)
    val dataState: StateFlow<DataState> = _dataState.asStateFlow()

    fun createBucket(name: String) {
        viewModelScope.launch {
            try {
                _dataState.value = DataState.Loading
                SupabaseClient.client.storage.createBucket(name) {
                    public = false
                    fileSizeLimit = 30.megabytes
                }
                _dataState.value = DataState.Success("Created bucket successfully!")
            } catch (e: Exception) {
                _dataState.value = DataState.Error("Creating bucket error: ${e.message}")
            }
        }
    }

    fun uploadFileToBucket(bucketName: String = "images", fileName: String, byteArray: ByteArray) {
        viewModelScope.launch {
            try {
                _dataState.value = DataState.Loading
                val bucket = SupabaseClient.client.storage[bucketName]
                bucket.upload("$fileName", byteArray, true)
                Log.d("tmp", "Upload to bucket successfully!")
                _dataState.value = DataState.Success("Upload to bucket successfully!")
            } catch (e: Exception) {
                Log.d("tmp", "Upload to bucket error: ${e.message}")
                _dataState.value = DataState.Error("Upload to bucket error: ${e.message}")
            }
        }
    }

//    /**
//     * для отпарвки нужен launcher
//     *
//     * val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
//     *         if (uri != null) {
//     *             uri.uriToBytesArray(context)?.let {
//     *                 viewModel.uploadFileToBucket("images", uri.path.toString().split("/").last() + ".jpg", it)
//     *             }
//     *         }
//     *     }
//     *
//     * его надо загрузить
//     *
//     * viewModel.createBucket("images")
//     * launcher.launch("image/*")
//     */

    fun getSignedUrlFromBucket(bucketName: String = "images", fileName: String, onImageUrlRetriever: (url: String?) -> Unit) {
        viewModelScope.launch {
            try {
                _dataState.value = DataState.Loading
                val bucket = SupabaseClient.client.storage[bucketName]
                val url = bucket.publicUrl("$fileName")
                onImageUrlRetriever(url)
                _dataState.value = DataState.Success("Read file from bucket successfully!")
            } catch (e: Exception) {
                onImageUrlRetriever(null)
                _dataState.value = DataState.Error("read from bucket error: ${e.message}")
            }
        }
    }
}
