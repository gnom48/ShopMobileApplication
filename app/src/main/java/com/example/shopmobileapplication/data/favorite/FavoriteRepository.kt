package com.example.shopmobileapplication.data.favorite


import com.example.shopmobileapplication.data.Favorite
import com.example.shopmobileapplication.data.Product
import com.example.shopmobileapplication.data.User
import com.example.shopmobileapplication.viewmodel.BaseRepository

interface FavoriteRepository: BaseRepository {
    suspend fun getProductsInFavorite(user: User): Result<List<Product>>

    suspend fun getFavoriteList(user: User): Result<List<Favorite>>

    suspend fun addProductToFavorite(favorite: Favorite): Result<Boolean>

    suspend fun deleteFavorite(favorite: Favorite): Result<Boolean>
}