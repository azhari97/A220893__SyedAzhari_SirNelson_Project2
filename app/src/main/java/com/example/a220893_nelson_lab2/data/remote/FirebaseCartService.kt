package com.example.a220893_nelson_lab2.data.remote

import android.util.Log
import com.example.a220893_nelson_lab2.data.viewmodels.CartItem
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await


class FirebaseCartService {
    private val firestore = FirebaseFirestore.getInstance()
    private val cartCollection = firestore.collection("carts")

    suspend fun getAllUserCartRecords(email: String): List<CartItem> {
        return try {
            val queryEmail = email.lowercase().trim()

            // Get orders where user is the buyer
            val buyerSnapshot = cartCollection.whereEqualTo("buyerId", queryEmail).get().await()
            val buyerItems = buyerSnapshot.toObjects(CartItem::class.java)

            // Get orders where user is the seller
            val sellerSnapshot = cartCollection.whereEqualTo("sellerId", queryEmail).get().await()
            val sellerItems = sellerSnapshot.toObjects(CartItem::class.java)

            // Merge lists together and drop any duplicate entries by ID
            (buyerItems + sellerItems).distinctBy { it.id }
        } catch (e: Exception) {
            Log.e("FIREBASE_CART", "Error fetching user cart records", e)
            emptyList()
        }
    }

    suspend fun addCartItem(cartItem: CartItem) {
        val freshDocRef = cartCollection.document()
        val finalizedItem = cartItem.copy(id = freshDocRef.id)
        freshDocRef.set(finalizedItem).await()
    }


    suspend fun updateCartItem(cartItem: CartItem) {
        if (cartItem.id.isBlank()) return
        cartCollection
            .document(cartItem.id)
            .set(cartItem)
            .await()
    }
}