package com.example.a220893_nelson_lab2.data.viewmodels

import android.util.Log
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.a220893_nelson_lab2.data.remote.FirebaseCartService
import com.example.a220893_nelson_lab2.data.repository.CartRepository
import com.example.a220893_nelson_lab2.data.repository.ProductRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
data class CartItem(
    val id: String = "",
    val productId: String = "",
    val sellerId: String = "",
    val buyerId: String = "",
    val dealMethod: String = "",
    val finalPrice: Double = 0.0,
    val meetLocation: String = "",
    val extraDetails: String = "",

    // Status Guardrails:
    // 0 = In Cart
    // 1 = Pending Approval
    // 2 = Accepted
    // 3 = Rejected
    // 4 = Complete
    val status: Int = 0
)

class CartViewModel(
    private val repository: CartRepository,
    private val productRepository: ProductRepository
) : ViewModel() {

    private val _cartItems = mutableStateListOf<CartItem>()

    val inCartItems = derivedStateOf {
        _cartItems.filter { it.status == 0 }
    }

    val transactionItems = derivedStateOf {
        _cartItems.filter { it.status > 0 }
    }

    fun loadCart(buyerEmail: String) {
        viewModelScope.launch {
            try {
                val cloudItems = repository.getItems(buyerEmail)
                _cartItems.clear()
                _cartItems.addAll(cloudItems)
            } catch (e: Exception) {
                Log.e("CART_VM", "Error syncing cart listings", e)
            }
        }
    }

    fun getCartItemById(id: String): CartItem? {
        return _cartItems.find { it.id == id }
    }

    fun addToCart(product: Product, offeredPrice: Double, buyerEmail: String) {
        viewModelScope.launch {
            val finalPrice = if (product.transactionType == "Donate") 0.0 else offeredPrice

            val newCartItem = CartItem(
                id = "",
                productId = product.id,
                sellerId = product.ownerId,
                buyerId = buyerEmail.lowercase().trim(),
                dealMethod = "Meetup",
                finalPrice = finalPrice,
                status = 0
            )
            try {
                repository.saveItem(newCartItem)
                loadCart(buyerEmail)
            } catch (e: Exception) {
                Log.e("CART_VM", "Failed to add item to cart", e)
            }
        }
    }

    fun loadAllUserRelatedCarts(userEmail: String) {
        viewModelScope.launch {
            try {
                val cleanedEmail = userEmail.lowercase().trim()
                val cloudItems = repository.getItems(cleanedEmail)

                _cartItems.clear()
                _cartItems.addAll(cloudItems)
            } catch (e: Exception) {
                Log.e("CART_VM", "Error syncing cart listings", e)
            }
        }
    }

    fun updateMeetLocation(cartId: String, meetLocation: String, extraDesc: String, buyerEmail: String) {
        viewModelScope.launch {
            val matchedItem = _cartItems.find { it.id == cartId } ?: return@launch
            val updatedItem = matchedItem.copy(
                meetLocation = meetLocation,
                extraDetails = extraDesc,
                status = 1
            )
            try {
                repository.updateItem(updatedItem)
                loadCart(buyerEmail)
            } catch (e: Exception) {
                Log.e("CART_VM", "Update meeting info error", e)
            }
        }
    }

    fun updateOfferPrice(cartId: String, newPrice: Double, buyerEmail: String) {
        viewModelScope.launch {
            val matchedItem = _cartItems.find { it.id == cartId } ?: return@launch
            val updatedItem = matchedItem.copy(finalPrice = newPrice, status = 1)
            try {
                repository.updateItem(updatedItem)
                loadAllUserRelatedCarts(buyerEmail)
            } catch (e: Exception) {
                Log.e("CART_VM", "Price counter-offer failed", e)
            }
        }
    }

    fun completeTransaction(cartId: String, buyerEmail: String) {
        viewModelScope.launch {
            val matchedItem = _cartItems.find { it.id == cartId } ?: return@launch
            val updatedItem = matchedItem.copy(status = 4)

            try {
                repository.updateItem(updatedItem)
                productRepository.softDeleteListing(matchedItem.productId)
                loadCart(buyerEmail)
            } catch (e: Exception) {
                Log.e("CART_VM", "Completion update failed", e)
            }
        }
    }

    //seller
    fun getIncomingOffersForSeller(sellerEmail: String): List<CartItem> {
        return _cartItems.filter {
            it.sellerId.lowercase() == sellerEmail.lowercase().trim() && it.status == 1
        }
    }

    fun respondToIncomingOffer(cartId: String, isAccepted: Boolean, userEmail: String) {
        viewModelScope.launch {
            val matchedItem = _cartItems.find { it.id == cartId } ?: return@launch

            val targetStatus = if (isAccepted) 2 else 3
            val updatedItem = matchedItem.copy(status = targetStatus)

            try {
                repository.updateItem(updatedItem)
                loadAllUserRelatedCarts(userEmail)
            } catch (e: Exception) {
                Log.e("CART_VM", "Seller response pipeline failed", e)
            }
        }
    }

}