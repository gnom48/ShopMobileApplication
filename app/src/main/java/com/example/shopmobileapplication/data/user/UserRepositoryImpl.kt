package com.example.shopmobileapplication.data.user

import android.content.Context
import com.example.shopmobileapplication.data.User
import com.example.shopmobileapplication.ui.viewmodel.UserViewModel
import com.example.shopmobileapplication.utils.SearchException
import com.example.shopmobileapplication.utils.SharedPreferecesHelper
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.gotrue.gotrue
import io.github.jan.supabase.gotrue.providers.builtin.Email
import io.github.jan.supabase.gotrue.user.UserInfo
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.MissingFieldException

class UserRepositoryImpl(
    private val context: Context,
    private val supabaseClient: SupabaseClient
): UserRepository {
    companion object {
        final val GUEST = User(
            id = "0",
            name = "Гость",
            image = null,
            address = null
        )

        private suspend fun getCurrentUserSelf(supabaseClient: SupabaseClient): User? = try {
            val userInfo = supabaseClient.gotrue.currentUserOrNull()
            getUserById(userInfo!!.id, supabaseClient)
        } catch (e: Exception) {
            null
        }

        private suspend fun getUserById(userId: String, supabaseClient: SupabaseClient): User? = try {
            supabaseClient.postgrest[User.tableName].select(filter = {
                User::id eq userId
            }).decodeSingle<User>()
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun getLocalToken(): String? = try {
        val sharedPreferences = SharedPreferecesHelper(context)
        sharedPreferences.getStringData(SharedPreferecesHelper.accessTokenKey)
    } catch (e: Exception) {
        null
    }

    override suspend fun getNewTokenAndSave(): String? = try {
        val accessToken = supabaseClient.gotrue.currentAccessTokenOrNull()

        val sharedPreferences = SharedPreferecesHelper(context)
        sharedPreferences.saveStringData(SharedPreferecesHelper.accessTokenKey, accessToken)

        accessToken
    } catch (e: Exception) {
        null
    }

    override suspend fun getCurrentUser(): Result<User> = try {
        val userInfo = supabaseClient.gotrue.currentUserOrNull()
        val user = userInfo?.let { getUserById(it.id, supabaseClient) }
        if (user != null) {
            Result.success(user)
        } else {
            Result.failure(SearchException.NotFoundException)
        }
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun getCurrentUserInfo(): Result<UserInfo> = try {
        Result.success(supabaseClient.gotrue.currentUserOrNull()!!)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun updateUserInfo(newPhone: String?, newEmail: String?, newPassword: String?, newImage: String?, newName: String?): Result<UserInfo> = try {
        supabaseClient.gotrue.modifyUser {
            newPhone?.let { phoneNumber = it }
            newEmail?.let { email = it }
            newPassword?.let { password = it }
        }

        supabaseClient.postgrest[User.tableName].update({
            newImage?.let { User::image setTo it }
            newName?.let { User::name setTo it }
        }) {
            User::id eq UserViewModel.currentUser.id
        }
        Result.success(supabaseClient.gotrue.currentUserOrNull()!!)
    } catch (e: Exception) {
        Result.failure(e)
    }

    private suspend fun getUserByUserInfo(userInfo: UserInfo?): User? = try {
        val user = userInfo?.let { getUserById(it.id, supabaseClient) }
        user ?: throw SearchException.NotFoundException
    } catch (e: Exception) {
        null
    }

    override suspend fun signUp(userName: String, userEmail: String, userPassword: String): Result<User> = try {
        supabaseClient.gotrue.signUpWith(Email) {
            email = userEmail
            password = userPassword
        }
        getNewTokenAndSave()

        val user = User(
            id = supabaseClient.gotrue.currentUserOrNull()!!.id,
            name = userName,
            image = null,
            address = null
        )

        supabaseClient.postgrest[User.tableName].insert(user, true)

        val sharedPreferencesHelper = SharedPreferecesHelper(context)
        sharedPreferencesHelper.saveStringData(SharedPreferecesHelper.lastEmailKey, userEmail)
        sharedPreferencesHelper.saveStringData(SharedPreferecesHelper.lastPasswordKey, userPassword)

        Result.success(user)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun signIn(userEmail: String, userPassword: String): Result<User> = try {
        supabaseClient.gotrue.loginWith(Email) {
            email = userEmail
            password = userPassword
        }
        getNewTokenAndSave()

        val sharedPreferencesHelper = SharedPreferecesHelper(context)
        sharedPreferencesHelper.saveStringData(SharedPreferecesHelper.lastEmailKey, userEmail)
        sharedPreferencesHelper.saveStringData(SharedPreferecesHelper.lastPasswordKey, userPassword)

        val user = getCurrentUserSelf(supabaseClient)?.let { getUserById(it.id, supabaseClient) }
        if (user != null) {
            Result.success(user)
        } else {
            Result.failure(SearchException.NotFoundException)
        }
    } catch (e: Exception) {
        Result.failure(e)
    }

    @OptIn(ExperimentalSerializationApi::class)
    override suspend fun anonSignIn(): Result<User> = try {
        supabaseClient.gotrue.signUpWith(Email) {
            email = ""
            password = ""
        }
        getNewTokenAndSave()
        Result.success(GUEST)
    } catch (e: MissingFieldException) {
        Result.success(GUEST)
    }catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun refreshSessionIfNeeds(): Boolean = try {
        val token = getLocalToken()
        if (token.isNullOrBlank()) {
            false
        } else {
            val userInfo = supabaseClient.gotrue.retrieveUser(token)
            getUserByUserInfo(userInfo)?.let { user -> UserViewModel.currentUser = user }
            supabaseClient.gotrue.refreshCurrentSession()
            getNewTokenAndSave()
            true
        }
    } catch (e: Exception) {
        false
    }

    override suspend fun signOut(): Boolean = try {
        supabaseClient.gotrue.logout()
        // TODO: возможно как-то удалять UserInfo
        SharedPreferecesHelper(context).deleteStringData(SharedPreferecesHelper.accessTokenKey)
        true
    } catch (e: Exception) {
        false
    }
}