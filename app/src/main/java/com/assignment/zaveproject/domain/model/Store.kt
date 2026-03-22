package com.assignment.zaveproject.domain.model

data class Store(
    val id: String,
    val name: String,
    val address: String,
    val lat: Double,
    val lng: Double,
    val type: String,
    val distance: Double,
    val rating: Double,
    val photoReference: String?
)
