package com.example.a220893_nelson_lab2.data.local.user

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.a220893_nelson_lab2.data.local.user.UserEntity

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(
        user:UserEntity
    )

    @Query("SELECT * FROM user WHERE id = 1 LIMIT 1")
    fun getUserFlow(): kotlinx.coroutines.flow.Flow<UserEntity?>
}