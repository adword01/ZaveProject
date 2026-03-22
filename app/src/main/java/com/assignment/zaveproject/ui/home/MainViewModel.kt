package com.assignment.zaveproject.ui.home

import android.Manifest
import android.content.Context
import android.location.Location
import android.util.Log
import androidx.annotation.RequiresPermission
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.assignment.zaveproject.helper.LocationHelper
import com.assignment.zaveproject.data.local.AppDatabase
import com.assignment.zaveproject.data.remote.PlacesApi
import com.assignment.zaveproject.data.repository.StoreRepo
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainViewModel(
    private val context: Context
) : ViewModel() {

    private val locationHelper = LocationHelper(context)
    private val remoteConfig = FirebaseRemoteConfig.getInstance()
    private val database = AppDatabase.getDatabase(context)

    private var remoteLoaded = false


    private val api = Retrofit.Builder()
        .baseUrl("https://maps.googleapis.com/maps/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(PlacesApi::class.java)

    private val repo = StoreRepo(api, database.dao, remoteConfig)

    private val _state = MutableStateFlow(MainState())
    val state: StateFlow<MainState> = _state

    init {
        val savedRadius = loadRadius()
        _state.update { it.copy(userRadius = savedRadius) }
        val autoLocation = loadAutoLocation()
        _state.update { it.copy(autoLocation = autoLocation) }
        loadLastLocation()
        loadSavedSettings()
        detectLocation()
        fetchRemoteConfig()
        loadRecentSearches()
    }

    fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.OnQueryChange -> {
                _state.update { it.copy(query = event.query) }
            }

            HomeEvent.OnSearchClick -> {
                searchStores()
            }

            is HomeEvent.OnLocationReceived -> {
                saveLastLocation(event.lat, event.lng)
                _state.update {
                    it.copy(
                        latitude = event.lat,
                        longitude = event.lng,
                        isLoadingLocation = false
                    )
                }
            }
        }
    }

    private fun saveLastLocation(lat: Double, lng: Double) {
        val prefs = context.getSharedPreferences("location", Context.MODE_PRIVATE)
        prefs.edit()
            .putFloat("lat", lat.toFloat())
            .putFloat("lng", lng.toFloat())
            .apply()
    }

    private fun loadLastLocation() {
        val prefs = context.getSharedPreferences("location", Context.MODE_PRIVATE)

        val lat = prefs.getFloat("lat", 0f)
        val lng = prefs.getFloat("lng", 0f)

        if (lat != 0f && lng != 0f) {
            _state.update {
                it.copy(
                    latitude = lat.toDouble(),
                    longitude = lng.toDouble()
                )
            }
        }
    }

//    private fun searchStores() {
//        val lat = _state.value.latitude ?: return
//        val lng = _state.value.longitude ?: return
//        val query = _state.value.query
//
//        if (query.isBlank()) return
//
//        saveSearch(query)
//
//        viewModelScope.launch {
//            _state.update { it.copy(isSearching = true, error = null) }
//            try {
//                val stores = repo.searchStores(query, lat, lng,_state.value.userRadius)
//                _state.update { it.copy(stores = stores, isSearching = false) }
//            } catch (e: Exception) {
//                _state.update { it.copy(error = e.message, isSearching = false) }
//            }
//        }
//    }

    private fun searchStores() {
        val lat = _state.value.latitude ?: return
        val lng = _state.value.longitude ?: return
        val query = _state.value.query.lowercase()

        if (query.isBlank()) return

        saveSearch(query)

        viewModelScope.launch {
            _state.update { it.copy(isSearching = true, error = null) }

            try {
                val apiStores = repo.searchStores(
                    query,
                    lat,
                    lng,
                    _state.value.userRadius
                )

                // If API returns empty -> fallback
                if (apiStores.isEmpty()) {
                    loadMockFilteredStores(query, lat, lng)
                } else {
                    _state.update {
                        it.copy(
                            stores = apiStores.sortedBy { store -> store.distance },
                            isSearching = false
                        )
                    }
                }

            } catch (e: Exception) {

                // REQUEST_DENIED or network error -> fallback
                loadMockFilteredStores(query, lat, lng)
            }
        }
    }



    @RequiresPermission(
        allOf = [
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ]
    )

    private fun loadMockFilteredStores(query: String, lat: Double, lng: Double) {

        val filtered = MockStoreData.stores
            .filter { it.type.contains(query) }
            .map { store ->
                val distance = calculateDistance(
                    lat,
                    lng,
                    store.lat,
                    store.lng
                )
                store.copy(distance = distance)
            }
            .filter { it.distance <= (_state.value.userRadius / 1000) }
            .sortedBy { it.distance }

        _state.update {
            it.copy(
                stores = filtered,
                isSearching = false
            )
        }
    }
    private fun detectLocation() {
        locationHelper.getCurrentLocation { location ->
            location?.let {
                onEvent(
                    HomeEvent.OnLocationReceived(
                        it.latitude,
                        it.longitude
                    )
                )
            }
        }
    }

//    private fun fetchRemoteConfig() {
//        remoteConfig.fetchAndActivate().addOnCompleteListener {
//            val banner = remoteConfig.getString("banner_message")
//            _state.update {
//                it.copy(banner = banner)
//            }
//        }
//    }
        private fun fetchRemoteConfig() {
            if (remoteLoaded) return
            remoteLoaded = true

            remoteConfig.setDefaultsAsync(
                mapOf(
                    "search_radius" to 3000,
                    "banner_message" to "Discover nearby stores",
                    "featured_category" to "Electronics"
                )
            )

            remoteConfig.fetchAndActivate().addOnCompleteListener {
                val banner = remoteConfig.getString("banner_message")
                val category = remoteConfig.getString("featured_category")

                _state.update {
                    it.copy(
                        banner = banner,
                        featuredCategory = category
                    )
                }
            }
        }

    private fun loadRecentSearches() {
        val prefs = context.getSharedPreferences("searches", Context.MODE_PRIVATE)
        val history = prefs.getStringSet("recent", emptySet())?.toList() ?: emptyList()

        _state.update {
            it.copy(lastSearches = history.takeLast(3).reversed())
        }
    }

    private fun saveSearch(query: String) {
        val prefs = context.getSharedPreferences("searches", Context.MODE_PRIVATE)
        val history = prefs.getStringSet("recent", mutableSetOf())?.toMutableSet()
            ?: mutableSetOf()

        history.add(query)
        prefs.edit().putStringSet("recent", history).apply()
        loadRecentSearches()
    }

//    fun updateRadius(radius: Int) {
//        saveRadius(radius)
//        _state.update {
//            it.copy(userRadius = radius)
//        }
//    }

    fun updateRadius(radius: Int) {
        _state.update { it.copy(userRadius = radius) }
    }


    private fun saveRadius(radius: Int) {
        val prefs = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
        prefs.edit().putInt("radius", radius).apply()
    }

    private fun loadRadius(): Int {
        val prefs = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
        return prefs.getInt("radius", 3000)
    }

    fun toggleAutoLocation(enabled: Boolean) {
        val prefs = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
        prefs.edit().putBoolean("auto_location", enabled).apply()

        _state.update {
            it.copy(autoLocation = enabled)
        }

        if (enabled) {
            detectLocation()
        }
    }
    private fun loadAutoLocation(): Boolean {
        val prefs = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
        return prefs.getBoolean("auto_location", true)
    }

    fun saveSettings() {

        val prefs = context.getSharedPreferences("app_settings", Context.MODE_PRIVATE)

        prefs.edit()
            .putInt("radius", _state.value.userRadius)
            .putBoolean("auto_location", _state.value.autoLocation)
            .apply()


        Log.d("SETTINGS", "Settings saved: radius=${_state.value.userRadius}, autoLocation=${_state.value.autoLocation}")
    }

    private fun loadSavedSettings() {

        val prefs = context.getSharedPreferences("app_settings", Context.MODE_PRIVATE)

        val radius = prefs.getInt("radius", 3000)
        val autoLocation = prefs.getBoolean("auto_location", true)

        _state.update {
            it.copy(
                userRadius = radius,
                autoLocation = autoLocation
            )
        }
    }

    fun saveStore(storeId: String) {
        viewModelScope.launch {
            database.dao.saveStore(storeId)
        }
    }

    fun loadMockStores() {
        _state.update {
            it.copy(
                stores = MockStoreData.stores,
            )
        }
    }
    fun calculateDistance(
        userLat: Double,
        userLng: Double,
        storeLat: Double,
        storeLng: Double
    ): Double {
        val result = FloatArray(1)
        Location.distanceBetween(
            userLat,
            userLng,
            storeLat,
            storeLng,
            result
        )
        return result[0].toDouble() / 1000
    }

//    private val repository = StoreRepo(api, dao, FirebaseRemoteConfig.getInstance())
}
