package com.example.shopmobileapplication.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.shopmobileapplication.data.User
import com.example.shopmobileapplication.data.user.UserRepository
import com.example.shopmobileapplication.utils.AuthException
import kotlinx.coroutines.launch

class UserViewModelFactory(private val userRepository: UserRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UserViewModel(userRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class UserViewModel(
    private val userRepository: UserRepository
) : BaseViewModel() {
    private val _user = mutableStateOf<User?>(null)
    val user by _user

    fun getLocalToken() {
        viewModelScope.launch {
            withLoading {
                if (userRepository.getLocalToken() == null) {
                    _error.value = AuthException.IncorrectLoginOrPasswordException
                }
            }
        }
    }

    fun getNewTokenAndSave() {
        viewModelScope.launch {
            withLoading {
                if (userRepository.getNewTokenAndSave() == null) {
                    _error.value = AuthException.IncorrectLoginOrPasswordException
                }
            }
        }
    }

    fun getCurrentUserInfo() {
        try {
            if (currentUser != null) {
                _user.value = currentUser
            }
        } catch (e: UninitializedPropertyAccessException) { }
        viewModelScope.launch {
            withLoading {
                userRepository.getCurrentUser().onSuccess { user ->
                    _user.value = user
                    _error.value = null
                    currentUser = user
                }.onFailure { e ->
                    _user.value = null
                    _error.value = e
                }
            }
        }
    }

    fun signUp(userName: String, userEmail: String, userPassword: String) {
        viewModelScope.launch {
            withLoading {
                userRepository.signUp(userName, userEmail, userPassword).onSuccess { user ->
                    _user.value = user
                    _error.value = null
                    currentUser = user
                }.onFailure { e ->
                    _user.value = null
                    _error.value = e
                }
            }
        }
    }

    fun signIn(userEmail: String, userPassword: String) {
        viewModelScope.launch {
            withLoading {
                userRepository.signIn(userEmail, userPassword).onSuccess { user ->
                    _user.value = user
                    currentUser = user
                    _error.value = null
                }.onFailure { e ->
                    _user.value = null
                    _error.value = e
                }
            }
        }
    }

    fun refreshSessionIfNeeds() {
        viewModelScope.launch {
            withLoading {
                if (!userRepository.refreshSessionIfNeeds()) {
                    _user.value = null
                } else {
                    getCurrentUserInfo()
                }
            }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            withLoading {
                if (userRepository.signOut()) {
                    _user.value = null
                }
            }
        }
    }

    companion object {
        lateinit var currentUser: User
    }
}