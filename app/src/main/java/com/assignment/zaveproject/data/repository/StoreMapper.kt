package com.assignment.zaveproject.data.repository

import com.assignment.zaveproject.domain.model.Store
import com.assignment.zaveproject.data.local.StoreEntity

fun Store.toEntity(query: String): StoreEntity {
    return StoreEntity(
        id = id,
        name = name,
        address = address,
        lat = lat,
        lng = lng,
        type = type,
        distance = distance,
        rating = rating,
        photoReference = photoReference,
        query = query
    )
}

fun StoreEntity.toModel(): Store {
    return Store(
        id = id,
        name = name,
        address = address,
        lat = lat,
        lng = lng,
        rating = rating,
        distance = distance,
        type = type,
        photoReference = photoReference
    )
}