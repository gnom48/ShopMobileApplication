package com.example.shopmobileapplication.data.user

import com.example.shopmobileapplication.data.User
import com.example.shopmobileapplication.ui.viewmodel.BaseRepository

interface UserRepository: BaseRepository {
    suspend fun getLocalToken(): String?

    suspend fun getNewTokenAndSave(): String?

    suspend fun getCurrentUser(): Result<User>

    suspend fun signUp(userName: String, userEmail: String, userPassword: String): Result<User>

    suspend fun signIn(userEmail: String, userPassword: String): Result<User>

    suspend fun refreshSessionIfNeeds(): Boolean

    suspend fun signOut(): Boolean
}