package com.example.a220893_nelson_lab2.data.remote.imgbb
import com.google.gson.annotations.SerializedName

data class ImgBbResponse(
    @SerializedName("data") val data: ImgBbData?,
    @SerializedName("success") val success: Boolean,
    @SerializedName("status") val status: Int
)

data class ImgBbData(
    @SerializedName("id") val id: String,
    @SerializedName("title") val title: String,
    @SerializedName("url") val url: String,
    @SerializedName("display_url") val displayUrl: String,
    @SerializedName("delete_url") val deleteUrl: String
)