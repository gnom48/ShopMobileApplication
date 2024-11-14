package com.example.shopmobileapplication.data.user

import android.content.Context
import android.util.Log
import com.example.shopmobileapplication.data.User
import com.example.shopmobileapplication.data.network.FirebaseClient
import com.example.shopmobileapplication.utils.AuthException
import com.example.shopmobileapplication.utils.SharedPreferecesHelper
import kotlinx.coroutines.tasks.await

class FirebaseUserRepositoryImpl(
    private val context: Context,
    private val firebaseClient: FirebaseClient = FirebaseClient
): UserRepository {
    companion object {
        private suspend fun getCurrentUserSelf(firebaseClient: FirebaseClient = FirebaseClient): User? = try {
            getUserById(firebaseClient.auth.currentUser!!.uid)
        } catch (e: Exception) {
            null
        }

        private suspend fun getUserById(userId: String, firebaseClient: FirebaseClient = FirebaseClient): User? = try {
            val t = firebaseClient.firestore.collection("users")
                .document(userId)
                .get()
                .await()

            val o = t.toObject(User::class.java)
            o
        } catch (e: Exception) {
            Log.e("tmp", e.message, e)
            null
        }
    }

    override suspend fun getLocalToken(): String? {
        return null
    }

    override suspend fun getNewTokenAndSave(): String? {
        return null
    }

    override suspend fun getCurrentUser(): Result<User> = try {
        if (firebaseClient.auth.currentUser != null) {
            val userDocument = firebaseClient.firestore.collection("users")
                .document(firebaseClient.auth.currentUser!!.uid)
                .get()
                .await()
            if (userDocument.exists()) {
                Result.success(userDocument.toObject(User::class.java)!!)
            } else {
                throw AuthException.NoCurrentUserException
            }
        } else {
            Result.failure(AuthException.NoCurrentUserException)
        }
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun signUp(
        userName: String,
        userEmail: String,
        userPassword: String,
    ): Result<User> = try {
        val authResult = firebaseClient.auth.createUserWithEmailAndPassword(userEmail, userPassword).await()
        val user = User(
            id = authResult.user!!.uid,
            name = userName,
            image = null
        )
        val userHashMap = mapOf(
            "id" to user.id,
            "name" to user.name,
            "image" to user.image
        )
        firebaseClient.firestore.collection("users")
            .document(user.id)
            .set(userHashMap)
            .await()
        val sharedPreferencesHelper = SharedPreferecesHelper(context)
        sharedPreferencesHelper.saveStringData(SharedPreferecesHelper.lastEmailKey, userEmail)
        sharedPreferencesHelper.saveStringData(SharedPreferecesHelper.lastPasswordKey, userPassword)
        Result.success(user)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun signIn(userEmail: String, userPassword: String): Result<User> = try {
        val authResult = firebaseClient.auth.signInWithEmailAndPassword(userEmail, userPassword)
            .await()
        val user = FirebaseUserRepositoryImpl.getUserById(authResult.user!!.uid, firebaseClient)
        Result.success(user!!)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun refreshSessionIfNeeds(): Boolean = try {
        firebaseClient.auth.signInWithCustomToken(getLocalToken()!!).await()
        true
    } catch (e: Exception) {
        false
    }

    override suspend fun signOut(): Boolean = try {
        firebaseClient.auth.signOut()
        true
    } catch (e: Exception) {
        false
    }
}