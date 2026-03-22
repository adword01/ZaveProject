package com.assignment.zaveproject.ui.settings

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.assignment.zaveproject.ui.home.MainViewModel

class SettingsActivity : ComponentActivity() {

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = MainViewModel(applicationContext)

        setContent {

            enableEdgeToEdge()

            val state by viewModel.state.collectAsState()

            SettingsScreen(
                state = state,
                onRadiusChange = { viewModel.updateRadius(it) },
                onToggleLocation = { viewModel.toggleAutoLocation(it) },
                onUpdateClick = { viewModel.saveSettings() }
            )
        }
    }
}