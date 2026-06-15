package com.example.a220893_nelson_lab2
import android.app.Application
import com.example.a220893_nelson_lab2.data.local.AppDatabase
import com.example.a220893_nelson_lab2.data.remote.FirebaseUserService
import com.example.a220893_nelson_lab2.data.repository.UserRepository
import com.example.a220893_nelson_lab2.data.repository.ProductRepository
import com.example.a220893_nelson_lab2.data.repository.CartRepository



class JSSApplication : Application() {

    val database by lazy { AppDatabase.getDatabase(this) }

    val userFirebaseService by lazy { FirebaseUserService() }
    val productRepository by lazy { ProductRepository() }
    val cartRepository by lazy {CartRepository()}
    val userRepository by lazy {
        UserRepository(
            userDao = database.userDao(),
            firebaseService = userFirebaseService
        )
    }

}