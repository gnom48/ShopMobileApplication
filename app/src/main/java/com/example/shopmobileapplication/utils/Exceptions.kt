package com.example.shopmobileapplication.utils

sealed class NumericException(msg: String): Exception(msg) {
    data object LessZeroException: NumericException("Number less zero")
}

sealed class AuthException(msg: String): Exception(msg) {
    data object NoSuchUserException: AuthException("No such user")
    data object IncorrectLoginOrPasswordException: AuthException("Incorrect login or password")
}

sealed class SearchException(msg: String): Exception(msg) {
    data object NotFoundException: SearchException("Object not found")
    data object IncorrectOperationException: SearchException("Incorrect operation")
}