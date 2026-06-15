package com.example.a220893_nelson_lab2.ui.screens.products

import android.R.attr.contentDescription
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.a220893_nelson_lab2.ui.screens.navigation.*
import com.example.a220893_nelson_lab2.data.viewmodels.ProductViewModel
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import com.example.a220893_nelson_lab2.data.viewmodels.CartViewModel
import com.example.a220893_nelson_lab2.data.viewmodels.UserViewModel
import androidx.compose.material3.Icon
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import com.example.a220893_nelson_lab2.R

@Composable
fun ProductDetailsScreen(
    navController: NavController,
    viewModel: ProductViewModel,
    cartViewModel: CartViewModel,
    userViewModel: UserViewModel,
    productId: String
) {
    val product = viewModel.getProductById(productId)

    val currentUserEmail = userViewModel.currentUser.value?.email ?: ""

    Scaffold(
        topBar = { TopBar(navController,true) },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->

        product?.let { prod ->
            val context = LocalContext.current
            var offerPrice by remember { mutableStateOf(prod.price.toString()) }
            var showDeleteDialog by remember { mutableStateOf(false) }
            val isOwnProduct = currentUserEmail.lowercase().trim() == prod.ownerId.lowercase().trim()

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                // Image Box Container
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                    )
                ) {
                    if(!product.imgUrl.isEmpty()){
                        AsyncImage(
                            model = product.imgUrl,
                            error =  painterResource(R.drawable.justsharestufflogo),
                            contentDescription = "Thumbnail for ${product.name}",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(240.dp),
                            contentScale = ContentScale.Crop
                        )
                    }else {
                    Image(
                        painter = painterResource(R.drawable.justsharestufflogo),
                        contentDescription = "Thumbnail for ${product.name}",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(240.dp)
                    )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainer
                    )
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(
                            text = prod.name,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = prod.description,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            AssistChip(onClick = { }, label = { Text(prod.type) })
                            AssistChip(onClick = { }, label = { Text(prod.transactionType) })
                            AssistChip(onClick = { }, label = { Text(prod.condition) })
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.primaryContainer
                        ) {
                            Text(
                                text = if (prod.transactionType == "Donate") "FREE / Donation" else "RM ${prod.price}",
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainer
                    )
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(
                            text = "Listing Details",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(shape = CircleShape, color = MaterialTheme.colorScheme.secondaryContainer) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = null,
                                    modifier = Modifier.padding(10.dp).size(24.dp),
                                    tint = MaterialTheme.colorScheme.onSecondaryContainer
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column {
                                Text(
                                    text = if (isOwnProduct) "Your Listing" else "Owner Contact Profile",
                                    style = MaterialTheme.typography.titleSmall,
                                    color = MaterialTheme.colorScheme.secondary
                                )
                                Text(
                                    text = prod.ownerId, // Directly utilizing the owner Email link string safely
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        if (isOwnProduct) {
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = MaterialTheme.colorScheme.tertiaryContainer,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "You are the owner of this marketplace listing.",
                                    modifier = Modifier.padding(12.dp),
                                    color = MaterialTheme.colorScheme.onTertiaryContainer,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                            Spacer(modifier = Modifier.height(16.dp))

                            Button(
                                onClick = { showDeleteDialog = true },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(52.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.error),
                                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.error)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = null,
                                    tint =  Color.White
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Delete Listing",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.SemiBold,
                                    color= Color.White
                                )
                            }
                        } else {
                            if (prod.transactionType != "Donate") {
                                OutlinedTextField(
                                    value = offerPrice,
                                    onValueChange = { input ->
                                        val filtered = input.filter { it.isDigit() || it == '.' }
                                        if (filtered.count { it == '.' } <= 1) {
                                            offerPrice = filtered
                                        }
                                    },
                                    label = { Text("Your Offer Price (RM)") },
                                    supportingText = {
                                        if (offerPrice.isEmpty()) {
                                            Text("Offer value cannot remain unassigned")
                                        }
                                    },
                                    singleLine = true,
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(12.dp)
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                Button(
                                    modifier = Modifier.fillMaxWidth().height(52.dp),
                                    onClick = {
                                        if (offerPrice.isNotEmpty() && currentUserEmail.isNotBlank()) {
                                            cartViewModel.addToCart(
                                                product = prod,
                                                offeredPrice = offerPrice.toDouble(),
                                                buyerEmail = currentUserEmail
                                            )
                                            navController.navigate("cart")
                                        }
                                    },
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Icon(Icons.Default.Send, null)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Send Offer", style = MaterialTheme.typography.titleMedium)
                                }
                            } else {
                                Button(
                                    modifier = Modifier.fillMaxWidth().height(52.dp),
                                    onClick = {
                                        if (currentUserEmail.isNotBlank()) {
                                            cartViewModel.addToCart(
                                                product = prod,
                                                offeredPrice = 0.0,
                                                buyerEmail = currentUserEmail
                                            )
                                            navController.navigate("cart")
                                        }
                                    },
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Icon(Icons.Default.Favorite, null)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Claim Donation Item", style = MaterialTheme.typography.titleMedium)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedButton(
                            onClick = { navController.popBackStack() },
                            modifier = Modifier.fillMaxWidth().height(50.dp),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Back to Explore")
                        }
                    }
                }
            }

            if (showDeleteDialog) {
                AlertDialog(
                    onDismissRequest = { showDeleteDialog = false },
                    title = {
                        Text(
                            text = "Delete Listing?",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    text = {
                        Text(
                            text = "Are you sure you want to remove this listing? It will no longer be visible to other members in the explore marketplace.",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                showDeleteDialog = false
                                viewModel.removeProductToUnlisted(prod.id)
                                navController.popBackStack()
                            }
                        ) {
                            Text(
                                text = "Delete",
                                color = MaterialTheme.colorScheme.error,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showDeleteDialog = false }) {
                            Text(
                                text = "Cancel",
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                )
            }
        }

    }
}