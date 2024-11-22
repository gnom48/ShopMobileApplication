package com.example.shopmobileapplication.data.favorite

import com.example.shopmobileapplication.data.Favorite
import com.example.shopmobileapplication.data.Product
import com.example.shopmobileapplication.data.User
import com.example.shopmobileapplication.data.network.SupabaseClient
import com.example.shopmobileapplication.data.product.ProductRepositoryImpl
import com.example.shopmobileapplication.utils.SharedPreferecesHelper
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class LocalFavoriteRepositoryImpl(
    private val sharedPreferencesHelper: SharedPreferecesHelper
): FavoriteRepository {
    private val favoritesListKey = "FavoriteList"

    private val gson = Gson()

    override suspend fun getProductsInFavorite(user: User): Result<List<Product>> = try {
        val json = sharedPreferencesHelper.getStringData(favoritesListKey)
        val favorites = if (json != null) { gson.fromJson<List<Favorite>>(json, object : TypeToken<List<Favorite>>() {}.type) } else emptyList<Favorite>()

        val products = mutableListOf<Product>()
        favorites.forEach { fav ->
            ProductRepositoryImpl.getOneProductById(fav.productId, SupabaseClient.client)?.let { products.add(it) }
        }
        Result.success(products.toList())
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun getFavoriteList(user: User): Result<List<Favorite>> = try {
        val json = sharedPreferencesHelper.getStringData(favoritesListKey)
        val res = if (json != null) { gson.fromJson<List<Favorite>>(json, object : TypeToken<List<Favorite>>() {}.type) } else emptyList<Favorite>()
        Result.success(res!!)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun addProductToFavorite(favorite: Favorite): Result<Boolean> = try {
        var json = sharedPreferencesHelper.getStringData(favoritesListKey)
        val favorites = if (json != null) { gson.fromJson<List<Favorite>>(json, object : TypeToken<List<Favorite>>() {}.type) } else emptyList<Favorite>()
        val f = favorites!!.toMutableList()
        f.add(favorite)
        json = gson.toJson(f)
        sharedPreferencesHelper.saveStringData(favoritesListKey, json)
        Result.success(true)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun deleteFavorite(favorite: Favorite): Result<Boolean> = try {
        var json = sharedPreferencesHelper.getStringData(favoritesListKey)
        val favorites = if (json != null) { gson.fromJson<List<Favorite>>(json, object : TypeToken<List<Favorite>>() {}.type) } else emptyList<Favorite>()
        val f = favorites!!.toMutableList()
        f.remove(favorite)
        json = gson.toJson(f)
        sharedPreferencesHelper.saveStringData(favoritesListKey, json)
        Result.success(true)
    } catch (e: Exception) {
        Result.failure(e)
    }
}