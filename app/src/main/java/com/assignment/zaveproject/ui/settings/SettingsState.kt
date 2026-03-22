package com.assignment.zaveproject.ui.settings

data class SettingsState(
    val radius: Int = 3000,
    val autoLocation: Boolean = true,
    val banner: String = "",
    val featuredCategory: String = "",
    val remoteLoaded: Boolean = false
)