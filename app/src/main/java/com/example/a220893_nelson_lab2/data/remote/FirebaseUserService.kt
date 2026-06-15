package com.example.a220893_nelson_lab2.data.remote

import android.util.Log
import com.example.a220893_nelson_lab2.data.viewmodels.User
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import kotlin.text.get

class FirebaseUserService {
    private val firestore = FirebaseFirestore.getInstance()
    private val userCollection = firestore.collection("users")

    suspend fun saveOrUpdateUser(user: User) {
        userCollection
            .document(user.email.lowercase().trim()) // Forces consistent lowercase ID lookups
            .set(user)
            .await()
    }

    suspend fun getUser(email: String): User? {
        val snapshot = userCollection.document(email.lowercase().trim()).get().await()
        return if (snapshot.exists()) {
            snapshot.toObject(User::class.java)
        } else {
            null
        }
    }

    suspend fun getAllUserRecords(): List<User> {
        return try {
            userCollection
                .get()
                .await()
                .toObjects(User::class.java)
        } catch (e: Exception) {
            android.util.Log.e("FIREBASE_USER", "Error fetching all user profiles", e)
            emptyList()
        }
    }

    suspend fun deleteUser(email: String) {
        userCollection
            .document(email.lowercase().trim())
            .delete()
            .await()
    }
}