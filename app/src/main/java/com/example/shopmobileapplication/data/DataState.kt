package com.example.shopmobileapplication.data

open class DataState {
    object Loading: DataState()
    object Nothing: DataState()
    data class Success(val message: String = ""): DataState()
    data class Error(val error: Throwable? = null): DataState()
}