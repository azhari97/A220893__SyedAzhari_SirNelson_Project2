package com.example.a220893_nelson_lab2.data.remote

import android.util.Log
import com.example.a220893_nelson_lab2.data.viewmodels.Product
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FirebaseProductService {
    private val firestore = FirebaseFirestore.getInstance()
    private val productCollection = firestore.collection("products")

    suspend fun getProducts(): List<Product> {
        return try {
            productCollection
                .whereEqualTo("unlisted", false)
                .get()
                .await()
                .toObjects(Product::class.java)
        } catch (e: Exception) {
            Log.e("FIREBASE_PRODUCT", "Error loading active listings", e)
            emptyList()
        }
    }

    suspend fun getOne(id: String): Product? {
        return try {
            val snapshot = productCollection.document(id).get().await()
            val product = snapshot.toObject(Product::class.java)

            // Check if the product exists
            if (product != null && !product.unlisted) product else null
        } catch (e: Exception) {
            Log.e("FIREBASE_PRODUCT", "Error getting single product entry", e)
            null
        }
    }
    suspend fun addProduct(product: Product) {
        // pre-generated secure random ID pointer from Firestore
        val freshDocRef = productCollection.document()
        //payload
        val finalizedProduct = product.copy(id = freshDocRef.id)
        // upload to firestone
        freshDocRef.set(finalizedProduct).await()
    }

    suspend fun updateProductToUnlisted(productId: String) {
        if (productId.isBlank()) return
        productCollection
            .document(productId)
            .update("unlisted", true)
            .await()
    }

    suspend fun deleteProduct(productId: String) {
        if (productId.isBlank()) return
        productCollection
            .document(productId)
            .delete()
            .await()
    }
}