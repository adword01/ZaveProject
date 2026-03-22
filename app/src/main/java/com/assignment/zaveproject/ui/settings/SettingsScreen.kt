package com.assignment.zaveproject.ui.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.assignment.zaveproject.ui.home.MainState

@Composable
fun SettingsScreen(
    state: MainState,
    onRadiusChange: (Int) -> Unit,
    onToggleLocation: (Boolean) -> Unit,
    onUpdateClick: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp,36.dp,16.dp,16.dp)
    ) {

        Text("Search Radius: ${state.userRadius} m")

        Slider(
            value = state.userRadius.toFloat(),
            onValueChange = {
                onRadiusChange(it.toInt())
            },
            valueRange = 1000f..10000f
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {

            Text("Use Location Automatically")

            Spacer(modifier = Modifier.weight(1f))

            Switch(
                checked = state.autoLocation,
                onCheckedChange = {
                    onToggleLocation(it)
                }
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text("Banner:")
        Text(state.banner)

        Spacer(modifier = Modifier.height(10.dp))

        Text("Featured Category:")
        Text(state.featuredCategory)

        Spacer(modifier = Modifier.height(30.dp))

        Button(
            onClick = onUpdateClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Update Settings")
        }
    }
}