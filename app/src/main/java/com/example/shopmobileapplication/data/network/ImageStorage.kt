package com.example.shopmobileapplication.data.network

object ImageStorage {
    private const val link = "http://31.129.102.158:5556/static/"

    fun getLink(imageName: String?): String = link + if (imageName.isNullOrBlank()) "default.png" else imageName
}