package com.assignment.zaveproject.data.remote

data class PlacesResponse(
    val results: List<PlaceResult>,
    val status: String,
    val rating: Double
)