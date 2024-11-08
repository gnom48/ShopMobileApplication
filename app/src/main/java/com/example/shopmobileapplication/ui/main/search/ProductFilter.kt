package com.example.shopmobileapplication.ui.main.search

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import com.example.shopmobileapplication.utils.NumericException

data class ProductFilter private constructor(
    val minPrice: Double? = null,
    val maxPrice: Double? = null,
    val sizes: List<Double>? = null,
    val stores: List<Int>? = null
) {
    class Builder {
        private var _minPrice = mutableStateOf<Double?>(null)
        private var _maxPrice = mutableStateOf<Double?>(null)
        private var _sizes = mutableStateOf<List<Double>?>(null)
        private var _stores = mutableStateOf<List<Int>?>(null)

        val minPriceValue by _minPrice
        val maxPriceValue by _maxPrice
        val sizesValue by _sizes
        val storesValue by _stores

        fun minPrice(minPrice: Double?) = apply { this._minPrice.value = minPrice }
        fun maxPrice(maxPrice: Double?) = apply { this._maxPrice.value = maxPrice }
        fun size(sizes: List<Double>?) = apply { this._sizes.value = sizes }
        fun removeSize(size: Double) = apply {
            val currentSizes = _sizes.value?.toMutableList() ?: mutableListOf()
            currentSizes.remove(size)
            _sizes.value = currentSizes
        }

        fun addSize(size: Double) = apply {
            val currentSizes = _sizes.value?.toMutableList() ?: mutableListOf()
            currentSizes.add(size)
            _sizes.value = currentSizes
        }

        fun storeIds(storeIds: List<Int>?) = apply { this._stores.value = storeIds }

        @Throws(NumericException.MinGTEMaxException::class)
        fun build(): ProductFilter {
            if (_minPrice.value != null && _maxPrice.value != null) {
                if (_minPrice.value!! >= _maxPrice.value!!) {
                    throw NumericException.MinGTEMaxException
                }
            }
            return ProductFilter(
                minPrice = _minPrice.value,
                maxPrice = _maxPrice.value,
                sizes = _sizes.value,
                stores = _stores.value
            )
        }
    }
}

