package com.assignment.zaveproject.ui.home

import com.assignment.zaveproject.domain.model.Store

//data class MainState(
//    val query: String = "",
//    val banner: String = "",
//    val lastSearches: List<String> = emptyList(),
//    val latitude: Double? = null,
//    val longitude: Double? = null,
//    val isLoadingLocation: Boolean = true,
//    val stores: List<Store> = emptyList(),
//    val isSearching: Boolean = false,
//    val error: String? = null
//)

data class MainState(
    val query: String = "",
    val stores: List<Store> = emptyList(),
    val latitude: Double? = null,
    val longitude: Double? = null,
    val banner: String = "",
    val featuredCategory: String = "",
    val lastSearches: List<String> = emptyList(),
    val userRadius: Int = 3000,
    val autoLocation: Boolean = true,
    val isSearching: Boolean = false,
    val isLoadingLocation: Boolean = true,
    val error: String? = null
)
