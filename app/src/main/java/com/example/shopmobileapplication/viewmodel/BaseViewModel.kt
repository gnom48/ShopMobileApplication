package com.example.shopmobileapplication.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

sealed class BaseViewModel: ViewModel() {
    protected val _error = mutableStateOf<Throwable?>(null)
    val error by _error

    fun dismissError() {
        _error.value = null
    }

    private val _isLoading = mutableStateOf<Boolean>(false)
    val isLoading by _isLoading

    suspend fun <T> withLoading(loadingState: MutableState<Boolean> = _isLoading, block: suspend () -> T): T {
        loadingState.value = true
        return try {
            block()
        } catch (e: Throwable) {
            _error.value = e
            throw e
        } finally {
            loadingState.value = false
        }
    }
}

interface BaseRepository { }
