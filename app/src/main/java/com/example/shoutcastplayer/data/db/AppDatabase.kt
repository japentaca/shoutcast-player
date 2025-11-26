package com.example.shoutcastplayer.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [FavoriteStation::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoritesDao(): FavoritesDao
}
