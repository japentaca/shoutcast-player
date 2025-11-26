package com.example.shoutcastplayer.di

import android.content.Context
import androidx.room.Room
import com.example.shoutcastplayer.data.db.AppDatabase
import com.example.shoutcastplayer.data.db.FavoritesDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "shoutcast_player.db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideFavoritesDao(database: AppDatabase): FavoritesDao {
        return database.favoritesDao()
    }
}
