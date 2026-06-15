package com.example.a220893_nelson_lab2.ui.screens.news

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.a220893_nelson_lab2.data.viewmodels.Article
import com.example.a220893_nelson_lab2.data.viewmodels.NewsUiState
import com.example.a220893_nelson_lab2.data.viewmodels.NewsViewModel
import com.example.a220893_nelson_lab2.R
import com.example.a220893_nelson_lab2.ui.screens.navigation.TopBar

@Composable
fun NewsScreen(newsViewModel: NewsViewModel,navController: NavController) {
    Scaffold(
        topBar = {
            TopBar(navController = navController,true)
        }
    ) { paddingValues ->
        Box(modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()) {
            when (val state = newsViewModel.uiState.value) {
                is NewsUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is NewsUiState.Success -> {
                    LazyColumn(contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        items(state.articles) { article ->
                            ArticleCard(article = article,type=2)
                        }
                    }
                }
                is NewsUiState.Error -> {
                    Text(
                        text = "Error: ${state.message}",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun ArticleCard(
    article: Article,
    modifier: Modifier = Modifier,
    type: Int = 1,
) {
    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column {
            if (!article.image.isNullOrEmpty()) {
                AsyncImage(
                    model = article.image,
                    error =  painterResource(R.drawable.justsharestufflogonews),
                    contentDescription = "News thumbnail for ${article.title}",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp),
                    contentScale = ContentScale.Crop
                )
            }else{
                Image(
                    painter = painterResource(R.drawable.justsharestufflogonews),
                    contentDescription = "App Logo",
                    modifier =   Modifier.fillMaxWidth()
                    .height(180.dp),
                    contentScale = ContentScale.Crop
                )
            }

            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = article.title ?: "No Title Available",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 2,
                    overflow = if(type==1)TextOverflow.Ellipsis else TextOverflow.Visible
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = article.author?.takeIf { it.isNotBlank() } ?: "Global SDG News",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = article.description ?: "No context snippet provided.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 3,
                    overflow = if(type==1)TextOverflow.Ellipsis else TextOverflow.Visible
                )
            }
        }
    }
}