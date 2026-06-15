package com.example.a220893_nelson_lab2.ui.screens.products

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import com.example.a220893_nelson_lab2.ui.screens.navigation.*

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.a220893_nelson_lab2.data.viewmodels.ProductViewModel
import com.example.a220893_nelson_lab2.data.viewmodels.UserViewModel
import com.example.a220893_nelson_lab2.ui.screens.navigation.TopBar
import com.example.a220893_nelson_lab2.ui.screens.searchbar.SearchBar
import com.example.a220893_nelson_lab2.ui.components.sectiontitle.*

@Composable
fun ExploreScreen(
    modifier: Modifier = Modifier,
    productViewModel: ProductViewModel,
    navController: NavController
) {
    var searchText by remember { mutableStateOf("") }
    var searchQuery by remember { mutableStateOf("") }

    Scaffold(
        topBar = { TopBar(navController) }
    ) { paddingValues ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            item { SectionTitle("Explore Items") }
            item { Spacer(modifier = Modifier.height(12.dp)) }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(modifier = Modifier.weight(1f)) {
                        SearchBar(
                            searchText = searchText,
                            onSearchChange = { searchText = it },
                            onSearchClick = {
                                searchQuery = searchText
                            },
                            onClearClick = {
                                searchText = ""
                                searchQuery = ""
                            }
                        )
                    }

                    FilledIconButton(
                        onClick = {
                            navController.navigate("addproduct")
                        },
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add new product listing"
                        )
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(12.dp)) }
            item { ProductGrid(searchQuery, navController, productViewModel) }
        }
    }
}