package com.example.shopmobileapplication.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.shopmobileapplication.data.Favorite
import com.example.shopmobileapplication.data.Product
import com.example.shopmobileapplication.data.User
import com.example.shopmobileapplication.data.favorite.FavoriteRepository
import kotlinx.coroutines.launch

class FavoriteViewModelFactory(private val favoriteRepository: FavoriteRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavoriteViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FavoriteViewModel(favoriteRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class FavoriteViewModel(
    private val favoriteRepository: FavoriteRepository
) : BaseViewModel() {
    private val _favorites = mutableStateOf<List<Favorite>>(emptyList())
    val favorites by _favorites

    suspend fun getFavoriteList(user: User) {
        viewModelScope.launch {
            withLoading {
                favoriteRepository.getFavoriteList(user).onSuccess {
                    _favorites.value = it
                    _error.value = null
                }.onFailure { e ->
                    _error.value = e
                    _favorites.value = emptyList()
                }
            }
        }
    }

    suspend fun addFavorite(favorite: Favorite) {
        withLoading {
            viewModelScope.launch {
                favoriteRepository.addProductToFavorite(favorite).onSuccess {
                    _error.value = null
                    _favorites.value.toMutableList().add(favorite)
                }.onFailure { e ->
                    _error.value = e
                }
            }
        }
    }

    suspend fun deleteFavorite(favorite: Favorite) {
        withLoading {
            viewModelScope.launch {
                favoriteRepository.deleteFavorite(favorite).onSuccess {
                    _error.value = null
                    _favorites.value.toMutableList().remove(favorite)
                }.onFailure { e ->
                    _error.value = e
                }
            }
        }
    }

    private val _productsInFavorite = mutableStateOf<List<Product>>(emptyList())
    val productsInFavorite by _productsInFavorite

    suspend fun getProductsInFavorite(user: User) {
        viewModelScope.launch {
            withLoading {
                favoriteRepository.getProductsInFavorite(user).onSuccess {
                    _productsInFavorite.value = it
                    _error.value = null
                }.onFailure { e ->
                    _error.value = e
                    _productsInFavorite.value = emptyList()
                }
            }
        }
    }
}