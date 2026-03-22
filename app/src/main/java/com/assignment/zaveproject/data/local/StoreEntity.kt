package com.assignment.zaveproject.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "stores")
data class StoreEntity(
    @PrimaryKey val id: String,
    val name: String,
    val address: String,
    val lat: Double,
    val lng: Double,
    val rating: Double,
    val distance: Double,
    val type: String,
    val photoReference: String?,
    val query: String,
    val isSaved: Boolean = false
)
