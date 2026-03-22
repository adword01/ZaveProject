package com.assignment.zaveproject.data.remote

data class PlaceResult(
    val place_id: String,
    val name: String,
    val vicinity: String,
    val types: List<String>,
    val geometry: Geometry,
    val rating: Double?,
    val photos: List<Photo>?
)