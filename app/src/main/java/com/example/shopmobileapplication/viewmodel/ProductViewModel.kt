package com.example.shopmobileapplication.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.shopmobileapplication.data.Product
import com.example.shopmobileapplication.data.ProductCategory
import com.example.shopmobileapplication.data.User
import com.example.shopmobileapplication.data.product.ProductRepository
import com.example.shopmobileapplication.ui.main.composable.CategoriesHelper
import kotlinx.coroutines.launch

class ProductViewModelFactory(private val productRepository: ProductRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProductViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProductViewModel(productRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class ProductViewModel(
    private val productRepository: ProductRepository
) : BaseViewModel() {
    private val _product = mutableStateOf<Product?>(null)
    val product by _product

    private val _products = mutableStateOf<List<Product>>(emptyList())
    val products by _products

    private val _favoriteProducts = mutableStateOf<List<Product>>(emptyList())
    val favoriteProducts by _favoriteProducts

    private val _bucketProducts = mutableStateOf<List<Product>>(emptyList())
    val bucketProducts by _bucketProducts

    suspend fun getProductById(id: String) {
        viewModelScope.launch {
            withLoading {
                if (_products.value.isNotEmpty()) {
                    val p = _products.value.find { it.id == id }
                    if (p != null) {
                        _product.value = p
                        return@withLoading
                    }
                }
                productRepository.getProductById(id).onSuccess {
                    _product.value = it
                    _error.value = null
                }.onFailure { e ->
                    _product.value = null
                    _error.value = e
                }
            }
        }
    }

    suspend fun getAllProducts(limit: Int? = null) {
        viewModelScope.launch {
            withLoading {
                productRepository.getAllProducts(limit).onSuccess {
                    _products.value = it
                }.onFailure { e ->
                    _products.value = emptyList()
                    _error.value = e
                }
            }
        }
    }

    private val _searchResults = mutableStateOf<List<Product>>(emptyList())
    val searchResults by _searchResults

    suspend fun getSearchResultsByProductName(query: String) {
        viewModelScope.launch {
            withLoading {
                productRepository.getSearchResultByProductName(query).onSuccess {
                    _searchResults.value = it
                }.onFailure { e ->
                    _searchResults.value = emptyList()
                    _error.value = e
                }
            }
        }
    }

    suspend fun getProductsByCategory(category: ProductCategory) {
        viewModelScope.launch {
            withLoading {
                if (category.id == 0) {
                    productRepository.getAllProducts().onSuccess {
                        _products.value = it
                    }.onFailure { e ->
                        _products.value = emptyList()
                        _error.value = e
                    }
                    return@withLoading
                }
                productRepository.getProductsByCategory(category).onSuccess {
                    _products.value = it
                }.onFailure { e ->
                    _products.value = emptyList()
                    _error.value = e
                }
            }
        }
    }

    suspend fun getProductsInBucket(user: User) {
        viewModelScope.launch {
            withLoading {
                productRepository.getProductsInBucket(user).onSuccess {
                    _bucketProducts.value = it
                }.onFailure { e ->
                    _bucketProducts.value = emptyList()
                    _error.value = e
                }
            }
        }
    }

    suspend fun getProductsInFavorite(user: User) {
        viewModelScope.launch {
            withLoading {
                productRepository.getProductsInFavorite(user).onSuccess {
                    _favoriteProducts.value = it
                }.onFailure { e ->
                    _favoriteProducts.value = emptyList()
                    _error.value = e
                }
            }
        }
    }

    private val _productCategories = mutableStateOf<List<ProductCategory>>(CategoriesHelper.defaultCategoriesList)
    val productCategories by _productCategories

    suspend fun getProductsCategories() {
        viewModelScope.launch {
            withLoading {
                productRepository.getProductsCategories().onSuccess {
                    _productCategories.value = CategoriesHelper.defaultCategoriesList
                    val updatedList = _productCategories.value.toMutableList()
                    updatedList.addAll(it)
                    _productCategories.value = updatedList
                    _error.value = null
                }.onFailure { e ->
                    _productCategories.value = emptyList()
                    _error.value = e
                }
            }
        }
    }
}