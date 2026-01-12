package com.example.shoutcastplayer.data.repository

import android.util.Log
import com.example.shoutcastplayer.data.api.RadioBrowserApi
import com.example.shoutcastplayer.data.db.FavoriteStation
import com.example.shoutcastplayer.data.db.FavoritesDao
import com.example.shoutcastplayer.data.model.StationDto
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RadioRepository @Inject constructor(
    private val api: RadioBrowserApi,
    private val favoritesDao: FavoritesDao
) {

    suspend fun getTopStations(): Result<List<StationDto>> {
        return try {
            val stations = api.getTopStations()
                .filter { it.bitrate > 0 }
            Log.d("SHOUTCAST", "RadioRepository: Fetched ${stations.size} top stations (filtered)")
            Result.success(stations)
        } catch (e: Exception) {
            Log.e("SHOUTCAST", "RadioRepository: Error fetching top stations: ${e.message}", e)
            Result.failure(e)
        }
    }

    suspend fun getStationsByTag(tag: String): Result<List<StationDto>> {
        return try {
            val stations = api.getStationsByTag(tag)
                .filter { it.bitrate > 0 }
            Result.success(stations)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun searchStations(name: String): Result<List<StationDto>> {
        return try {
            val stations = api.searchStations(name)
                .filter { it.bitrate > 0 }
            Result.success(stations)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getTags(): Result<List<com.example.shoutcastplayer.data.model.TagDto>> {
        return try {
            val tags = api.getTags()
            Result.success(tags)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getAllFavorites(): Flow<List<FavoriteStation>> = favoritesDao.getAllFavorites()

    suspend fun isFavorite(stationUuid: String): Boolean {
        return favoritesDao.getFavoriteById(stationUuid) != null
    }

    suspend fun addFavorite(station: StationDto) {
        val favorite = FavoriteStation(
            stationUuid = station.stationUuid,
            name = station.name,
            url = station.urlResolved,
            homepage = station.homepage,
            favicon = station.favicon,
            tags = station.tags,
            country = station.country,
            bitrate = station.bitrate
        )
        favoritesDao.insertFavorite(favorite)
    }

    suspend fun removeFavorite(stationUuid: String) {
        favoritesDao.deleteFavoriteById(stationUuid)
    }
}
