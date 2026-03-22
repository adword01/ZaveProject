package com.assignment.zaveproject.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface PlacesApi {

    @GET("place/nearbysearch/json")
    suspend fun searchNearby(
        @Query("location") location: String,
        @Query("radius") radius: Int,
        @Query("type") type: String = "store",
        @Query("keyword") keyword: String,
        @Query("key") apiKey: String
    ): PlacesResponse
}