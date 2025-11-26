package com.example.shoutcastplayer.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class FavoriteStation(
    @PrimaryKey val stationUuid: String,
    val name: String,
    val url: String,
    val homepage: String,
    val favicon: String,
    val tags: String,
    val country: String,
    val bitrate: Int
)
