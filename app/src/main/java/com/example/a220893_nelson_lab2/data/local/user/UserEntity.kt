package com.example.a220893_nelson_lab2.data.local.user

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class UserEntity(
    @PrimaryKey
    val id: Int = 1,
    val pImageUrl: String = "",
    val firebaseUid:String = "",
    val name: String = "",
    val email: String = ""

)