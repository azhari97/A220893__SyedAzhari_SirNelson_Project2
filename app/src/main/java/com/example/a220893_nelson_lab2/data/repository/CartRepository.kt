package com.example.a220893_nelson_lab2.data.repository

import com.example.a220893_nelson_lab2.data.viewmodels.CartItem
import com.example.a220893_nelson_lab2.data.remote.FirebaseCartService

class CartRepository(
    private val firebaseService: FirebaseCartService = FirebaseCartService()
) {
    suspend fun getItems(userEmail: String): List<CartItem> = firebaseService.getAllUserCartRecords(userEmail)
    suspend fun saveItem(cartItem: CartItem) = firebaseService.addCartItem(cartItem)
    suspend fun updateItem(cartItem: CartItem) = firebaseService.updateCartItem(cartItem)
}