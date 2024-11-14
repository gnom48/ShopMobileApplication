package com.example.shopmobileapplication.data.network

import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

object FirebaseClient {
    val firestore = Firebase.firestore
    val auth = Firebase.auth
}