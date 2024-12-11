package com.example.shopmobileapplication.data.user

import com.example.shopmobileapplication.data.User
import com.example.shopmobileapplication.ui.viewmodel.BaseRepository
import io.github.jan.supabase.gotrue.user.UserInfo

interface UserRepository: BaseRepository {
    suspend fun getLocalToken(): String?
    suspend fun getNewTokenAndSave(): String?
    suspend fun getCurrentUser(): Result<User>
    suspend fun signUp(userName: String, userEmail: String, userPassword: String): Result<User>
    suspend fun signIn(userEmail: String, userPassword: String): Result<User>
    suspend fun anonSignIn(): Result<User>
    suspend fun refreshSessionIfNeeds(): Boolean
    suspend fun signOut(): Boolean
    suspend fun getCurrentUserInfo(): Result<UserInfo>
    suspend fun updateUserInfo(newPhone: String? = null, newEmail: String? = null, newPassword: String? = null, newImage: String? = null, newName: String? = null): Result<UserInfo>
}