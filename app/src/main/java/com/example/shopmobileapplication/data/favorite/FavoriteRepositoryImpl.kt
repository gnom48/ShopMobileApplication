package com.example.shopmobileapplication.data.favorite

import android.content.Context
import com.example.shopmobileapplication.data.Favorite
import com.example.shopmobileapplication.data.Product
import com.example.shopmobileapplication.data.User
import com.example.shopmobileapplication.data.product.ProductRepositoryImpl
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest

class FavoriteRepositoryImpl(
    private val context: Context,
    private val supabaseClient: SupabaseClient
): FavoriteRepository {
    override suspend fun getProductsInFavorite(user: User): Result<List<Product>> = try {
        val favorites = supabaseClient.postgrest["favorites"].select(filter = {
            Favorite::userId eq user.id
        }).decodeList<Favorite>()
        val products = mutableListOf<Product>()
        favorites.forEach { fav ->
            ProductRepositoryImpl.getOneProductById(fav.productId, supabaseClient)
                ?.let { products.add(it) }
        }
        Result.success(products.toList())
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun getFavoriteList(user: User): Result<List<Favorite>> = try {
        val favorites = supabaseClient.postgrest["favorites"].select(filter = {
            Favorite::userId eq user.id
        }).decodeList<Favorite>()
        Result.success(favorites)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun addProductToFavorite(favorite: Favorite): Result<Boolean> = try {
        supabaseClient.postgrest["favorites"].insert(favorite)
        Result.success(true)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun deleteFavorite(favorite: Favorite): Result<Boolean> = try {
        supabaseClient.postgrest["favorites"].delete(filter = {
            and {
                Favorite::productId eq favorite.productId
                Favorite::userId eq favorite.userId
            }
        })
        Result.success(true)
    } catch (e: Exception) {
        Result.failure(e)
    }
}