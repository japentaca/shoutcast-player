package com.example.shoutcastplayer.data.api

import com.example.shoutcastplayer.data.model.StationDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RadioBrowserApi {

    @GET("json/stations/topvote/{limit}")
    suspend fun getTopStations(@Path("limit") limit: Int = 20): List<StationDto>

    @GET("json/stations/bytag/{tag}")
    suspend fun getStationsByTag(
        @Path("tag") tag: String,
        @Query("limit") limit: Int = 50,
        @Query("order") order: String = "votes",
        @Query("reverse") reverse: Boolean = true
    ): List<StationDto>

    @GET("json/stations/search")
    suspend fun searchStations(
        @Query("name") name: String,
        @Query("limit") limit: Int = 50,
        @Query("order") order: String = "votes",
        @Query("reverse") reverse: Boolean = true
    ): List<StationDto>
}
