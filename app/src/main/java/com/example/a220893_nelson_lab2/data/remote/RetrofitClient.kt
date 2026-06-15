package com.example.a220893_nelson_lab2.data.remote

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import com.example.a220893_nelson_lab2.BuildConfig
import com.example.a220893_nelson_lab2.data.remote.imgbb.ImgBbApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.ByteArrayOutputStream
import java.io.InputStream

//    private val logging = HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
//        override fun log(message: String) {
//            val trimmed = message.trim()
//
//            // Check if the message is a JSON Object or Array
//            if (trimmed.startsWith("{") && trimmed.endsWith("}")) {
//                runCatching { println(JSONObject(trimmed).toString(4)) }
//                    .onFailure { println(message) }
//            } else if (trimmed.startsWith("[") && trimmed.endsWith("]")) {
//                runCatching { println(JSONArray(trimmed).toString(4)) }
//                    .onFailure { println(message) }
//            } else {
//                // Log non-JSON lines (headers, URLs, status codes) normally
//                println(message)
//            }
//        }
//    }).apply {
//        level = HttpLoggingInterceptor.Level.BODY
//    }
object RetrofitClient {
    private val logging = HttpLoggingInterceptor().apply {
        // LEVEL OPTIONS:
        // .Level.BASIC -> Logs request method, URL, and response code
        // .Level.HEADERS -> Logs headers + basic
        // .Level.BODY -> Logs everything (URL, Headers, and the full JSON Body)
        level = HttpLoggingInterceptor.Level.BODY
    }
    private val httpClient = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()

    private const val BASE_URL = "https://api.currentsapi.services/"
    val newsApiService: NewsApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NewsApiService::class.java)
    }

    private const val IMGBB_URL = "https://api.imgbb.com/"
    private const val IMGBB_API_KEY = BuildConfig.IMGBB_API_KEY

    private val imgUploadService: ImgBbApiService by lazy {
        Retrofit.Builder()
            .baseUrl(IMGBB_URL)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ImgBbApiService::class.java)
    }
    suspend fun uploadProductBitmap(bitmap: Bitmap): String? = withContext(Dispatchers.IO) {
        try {
            // 1. Compress the in-memory bitmap directly into a JPEG byte array output stream
            val outputStream = ByteArrayOutputStream()
            bitmap.compress(
                Bitmap.CompressFormat.JPEG,
                90,
                outputStream
            )
            val imageBytes = outputStream.toByteArray()

            // 2. get data into RequestBody
            val requestFile =
                imageBytes.toRequestBody("image/jpeg".toMediaTypeOrNull(), 0, imageBytes.size)

            // 3.dynamic filename format (product_YYYYMMDD_HHMMSS.jpg)
            val timestamp =
                java.text.SimpleDateFormat("yyyyMMdd_HHmmss", java.util.Locale.getDefault())
                    .format(java.util.Date())
            val dynamicFileName = "product_$timestamp.jpg"

            val multipartBodyFile =
                MultipartBody.Part.createFormData("image", dynamicFileName, requestFile)

            // 4. network request to imgbb and get response
            val networkResponse =
                imgUploadService.uploadImage(apiKey = IMGBB_API_KEY, image = multipartBodyFile)
                Log.e("IMGBB_UPLOAD", "Server Error: ${networkResponse.errorBody()?.string()}")

            if (networkResponse.isSuccessful) {
                return@withContext networkResponse.body()?.data?.url
            } else {
                Log.e("RETROFIT_UPLOAD", "Server Error: ${networkResponse.errorBody()?.string()}")
                return@withContext null
            }
        } catch (e: Exception) {
            Log.e("RETROFIT_UPLOAD", "Upload crash", e)
            return@withContext null
        }
    }
}