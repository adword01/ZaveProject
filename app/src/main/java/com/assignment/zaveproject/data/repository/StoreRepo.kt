package com.assignment.zaveproject.data.repository

import android.location.Location
import android.util.Log
import com.assignment.zaveproject.BuildConfig
import com.assignment.zaveproject.ui.home.MockStoreData
import com.assignment.zaveproject.domain.model.Store
import com.assignment.zaveproject.data.local.StoreDao
import com.assignment.zaveproject.data.remote.PlacesApi
import com.google.firebase.remoteconfig.FirebaseRemoteConfig

class StoreRepo(
    private val api: PlacesApi,
    private val dao: StoreDao,
    private val remoteConfig: FirebaseRemoteConfig
) {

    suspend fun searchStores(
        query: String,
        lat: Double,
        lng: Double,
        radius: Int
    ): List<Store> {

        val finalRadius =
            if (radius > 0) radius
            else remoteConfig.getLong("search_radius")
                .takeIf { it > 0 }?.toInt() ?: 3000


//        val radius = remoteConfig.getLong("search_radius")
//            .takeIf { it > 0 }?.toInt() ?: 3000

        return try {

            val response = api.searchNearby(
                location = "$lat,$lng",
                radius = finalRadius,
                keyword = "$query store",
                apiKey = BuildConfig.MAPS_API_KEY

            )

            if (response.status == "REQUEST_DENIED") {
                return loadMockStores(query)
            }

            Log.d("PLACES_DEBUG", "Location: $lat,$lng")
            Log.d("PLACES_DEBUG", "Radius used: $finalRadius")
            Log.d("PLACES_DEBUG", "Query: $query")
            Log.d("PLACES_DEBUG", "Status: ${response.status}")
            Log.d("PLACES_DEBUG", "Results count: ${response.results.size}")


//            val stores = response.results.map {
//                Store(
//                    id = it.place_id,
//                    name = it.name,
//                    address = it.vicinity,
//                    lat = it.geometry.location.lat,
//                    lng = it.geometry.location.lng,
//                    distance = 0.0,
//                    type = it.types.firstOrNull() ?: "",
//                    photoReference = it.photos?.firstOrNull()?.photo_reference
//                )
//            }
            val stores = response.results.map {

                val distance = calculateDistance(
                    lat,
                    lng,
                    it.geometry.location.lat,
                    it.geometry.location.lng
                )

                val rating = it.rating ?: 0.0


                Store(
                    id = it.place_id,
                    name = it.name,
                    address = it.vicinity,
                    lat = it.geometry.location.lat,
                    lng = it.geometry.location.lng,
                    type = it.types.firstOrNull() ?: "",
                    distance = distance,
                    rating = rating,
                    photoReference = it.photos?.firstOrNull()?.photo_reference
                )
            }

            dao.insertStores(stores.map { it.toEntity(query) })

            val finalStores = stores
                .filter { it.type.contains(query, ignoreCase = true) }
                .sortedBy { it.distance }

            finalStores

        } catch (e: Exception) {

            dao.getStores(query).map { it.toModel() }

        }
    }

    fun calculateDistance(
        userLat: Double,
        userLng: Double,
        placeLat: Double,
        placeLng: Double
    ): Double {

        val result = FloatArray(1)

        Location.distanceBetween(
            userLat,
            userLng,
            placeLat,
            placeLng,
            result
        )

        return (result[0] / 1000.0)

    }
    private fun loadMockStores(query: String): List<Store> {
        return MockStoreData.stores
            .filter { it.type == query }
            .sortedBy { it.distance }
    }
}