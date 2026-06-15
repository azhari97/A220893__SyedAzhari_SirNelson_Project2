package com.example.a220893_nelson_lab2.data.viewmodels

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.a220893_nelson_lab2.data.remote.RetrofitClient
import kotlinx.coroutines.launch
import com.example.a220893_nelson_lab2.BuildConfig
import kotlinx.serialization.Serializable

data class NewsResponse(
    val status: String,
    val news: List<Article>
)

data class Article(
    val id: String? = null,
    val title: String? = null,
    val description: String? = null,
    val url: String? = null,
    val author: String? = null,
    val image: String? = null,
    val published: String? = null
)

data class Source(
    val name: String?
)
interface NewsUiState {
    object Loading : NewsUiState
    data class Success(val articles: List<Article>) : NewsUiState
    data class Error(val message: String) : NewsUiState
}

class NewsViewModel : ViewModel() {
    private val _uiState = mutableStateOf<NewsUiState>(NewsUiState.Loading)
    val uiState: State<NewsUiState> = _uiState
        private val apiKey = BuildConfig.NEWS_API_KEY
    init {
        fetchSdg12News()
    }

    fun fetchSdg12News() {
        viewModelScope.launch {
            _uiState.value = NewsUiState.Loading
            try {
                val response = RetrofitClient.newsApiService.getNews(
                    apiKey = apiKey
                )
                Log.d("NEWS_API", "Server connected. Array list size: ${response.news?.size}")
                response.news.forEachIndexed { index, article ->
                    Log.d("NEWS_API", "Item #$index | Title: ${article.title} | URL: ${article.url}")
                }
                _uiState.value = NewsUiState.Success(response.news)
            } catch (e: Exception) {
                Log.e("NEWS_API", "Error loading payload structure", e)
                _uiState.value = NewsUiState.Error(e.localizedMessage ?: "Network connection failed")
            }
        }
    }

    fun getArticles(): List<Article> {
        val currentState = _uiState.value
        return if (currentState is NewsUiState.Success) {
            currentState.articles
        } else {
            emptyList()
        }
    }
}