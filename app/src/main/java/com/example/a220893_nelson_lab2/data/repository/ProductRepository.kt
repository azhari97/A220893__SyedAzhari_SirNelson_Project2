package com.example.a220893_nelson_lab2.data.repository

import com.example.a220893_nelson_lab2.data.remote.FirebaseProductService
import com.example.a220893_nelson_lab2.data.viewmodels.Product
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class ProductRepository {
    private val firebaseService: FirebaseProductService = FirebaseProductService()

    suspend fun getItems(): List<Product> = firebaseService.getProducts()

    suspend fun getOne(id: String): Product? = firebaseService.getOne(id)

    suspend fun save(product: Product) = firebaseService.addProduct(product)

    suspend fun softDeleteListing(productId: String) = firebaseService.updateProductToUnlisted(productId)

    suspend fun permanentDeleteProduct(productId: String) = firebaseService.deleteProduct(productId)
}