package com.example.a220893_nelson_lab2.data.repository

import com.example.a220893_nelson_lab2.data.local.user.UserDao
import com.example.a220893_nelson_lab2.data.local.user.UserEntity
import com.example.a220893_nelson_lab2.data.remote.FirebaseUserService
import com.example.a220893_nelson_lab2.data.viewmodels.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserRepository(

    private val userDao: UserDao,
    private val firebaseService: FirebaseUserService
) {

    // Expose the local Room data stream to let MainActivity track login state dynamically
    val localUserFlow: Flow<User?> = userDao.getUserFlow().map { entity ->
        entity?.let { User(email = it.email, name = it.name, pImageUrl = it.pImageUrl) }
    }

    suspend fun getAllUser(): List<User> {
        return firebaseService.getAllUserRecords()
    }
    suspend fun registerNewUser(name: String, email: String) {
        val user = User(name = name, email = email)
        firebaseService.saveOrUpdateUser(user)
        userDao.insertUser(UserEntity(id = 1, name = name, email = email))
    }

    suspend fun loginWithEmail(email: String): Boolean {
        val cloudUser = firebaseService.getUser(email)
        return if (cloudUser != null) {
            userDao.insertUser(UserEntity(id = 1, name = cloudUser.name, email = cloudUser.email))
            true
        } else {
            false
        }
    }

    suspend fun updateCrudProfile(name: String, email: String) {
        val updatedUser = User(name = name, email = email)
        firebaseService.saveOrUpdateUser(updatedUser)
        userDao.insertUser(UserEntity(id = 1, name = name, email = email))
    }

    suspend fun clearLocalCacheAndLogout() {
        // Overwrite Row 1 inside Room with empty fields to clear the cached state
        userDao.insertUser(UserEntity(id = 1, name = "", email = ""))
    }
}