package com.example.a220893_nelson_lab2.data.viewmodels

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel

data class NavItem(
    val label: String,
    val icon: ImageVector,
    val route: String
)
class NavViewModel : ViewModel() {

    val navItems = listOf(
        NavItem("Home", Icons.Default.Home, "home"),
        NavItem("Explore", Icons.Default.Search, "explore"),
        NavItem("Add", Icons.Default.Add, "addproduct"),
        NavItem("Cart", Icons.Default.ShoppingCart, "cart"),
        NavItem("Profile", Icons.Default.Person, "profile")
    )

}