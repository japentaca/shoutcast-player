package com.example.shoutcastplayer.data.model

import com.google.gson.annotations.SerializedName

data class TagDto(
    @SerializedName("name") val name: String,
    @SerializedName("stationcount") val stationCount: Int
)
