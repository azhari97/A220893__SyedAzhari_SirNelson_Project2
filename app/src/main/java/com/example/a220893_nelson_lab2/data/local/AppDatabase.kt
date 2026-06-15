package com.example.a220893_nelson_lab2.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.a220893_nelson_lab2.data.local.user.UserDao
import com.example.a220893_nelson_lab2.data.local.user.UserEntity

@Database(entities = [UserEntity::class],
    version = 1,
    exportSchema = false)
abstract class AppDatabase : RoomDatabase(){
    abstract fun userDao(): UserDao
//    abstract fun cartDao(): CartDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: android.content.Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = androidx.room.Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
