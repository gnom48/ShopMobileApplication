package com.example.shopmobileapplication.utils

import android.content.Context

class SharedPreferecesHelper(private val context: Context) {

    companion object{
        private const val MY_PREF_KEY = "MY_PREF"
        const val accessTokenKey: String = "accessToken"
        const val lastEmailKey: String = "lastEmail"
        const val lastPasswordKey: String = "lastPassword"
        const val seenOnBoardKey: String = "seenOnBoard"
    }

    fun saveStringData(key: String, data: String?) {
        val sharedPreferences = context.getSharedPreferences(MY_PREF_KEY, Context.MODE_PRIVATE)
        sharedPreferences.edit().putString(key, data).apply()
    }

    fun getStringData(key: String): String? {
        val sharedPreferences = context.getSharedPreferences(MY_PREF_KEY, Context.MODE_PRIVATE)
        return sharedPreferences.getString(key, null)
    }

    fun deleteStringData(key: String): Boolean? {
        try {
            val sharedPreferences = context.getSharedPreferences(MY_PREF_KEY, Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.remove(key)
            editor.apply()
            return true
        } catch (e: Exception) {
            return false
        }
    }

    fun clearPreferences() {
        val sharedPreferences = context.getSharedPreferences(MY_PREF_KEY, Context.MODE_PRIVATE)
        return sharedPreferences.edit().clear().apply()
    }
}