package com.assignment.zaveproject.ui.home

sealed class HomeEvent {
    data class OnQueryChange(val query: String) : HomeEvent()
    object OnSearchClick : HomeEvent()
    data class OnLocationReceived(val lat: Double, val lng: Double) : HomeEvent()
}