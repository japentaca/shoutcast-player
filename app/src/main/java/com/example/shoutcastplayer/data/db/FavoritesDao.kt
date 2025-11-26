package com.example.shoutcastplayer.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoritesDao {
    @Query("SELECT * FROM favorites")
    fun getAllFavorites(): Flow<List<FavoriteStation>>

    @Query("SELECT * FROM favorites WHERE stationUuid = :uuid")
    suspend fun getFavoriteById(uuid: String): FavoriteStation?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(station: FavoriteStation)

    @Delete
    suspend fun deleteFavorite(station: FavoriteStation)
    
    @Query("DELETE FROM favorites WHERE stationUuid = :uuid")
    suspend fun deleteFavoriteById(uuid: String)
}
