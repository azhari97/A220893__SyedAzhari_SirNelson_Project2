package com.example.a220893_nelson_lab2.data.remote

import com.example.a220893_nelson_lab2.data.viewmodels.NewsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApiService {
    @GET("v1/search")
    suspend fun getNews(
        @Query("query") query: String = "sdg 12",
        @Query("language") language: String = "en",
        @Query("page_size") pageSize: Int = 5,
        @Query("apiKey") apiKey: String
    ): NewsResponse
}