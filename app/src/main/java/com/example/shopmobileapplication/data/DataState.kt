package com.example.shopmobileapplication.data

open class DataState {
    object Loading: DataState()
    data class Success(val message: String): DataState()
    data class Error(val message: String): DataState()
}