package com.assignment.zaveproject.ui.home

import android.content.Intent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.assignment.zaveproject.ui.settings.SettingsActivity

@Composable
fun HomeScreen(viewModel: MainViewModel) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .statusBarsPadding()
    ) {

        // Banner + Settings Button
        if (state.banner.isNotEmpty()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = state.banner,
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                IconButton(
                    onClick = {
                        val intent = Intent(context, SettingsActivity::class.java)
                        context.startActivity(intent)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Settings"
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Search Bar
        OutlinedTextField(
            value = state.query,
            onValueChange = { viewModel.onEvent(HomeEvent.OnQueryChange(it)) },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Search product or category") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Search Button
        Button(
            onClick = { viewModel.onEvent(HomeEvent.OnSearchClick) },
            modifier = Modifier.fillMaxWidth(),
            enabled = !state.isSearching && state.latitude != null
        ) {
            if (state.isSearching) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = Color.White
                )
            } else {
                Text("Search Nearby Stores")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Recent Searches
        if (state.lastSearches.isNotEmpty()) {
            Text(
                text = "Recent Searches",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.secondary
            )

            Row(modifier = Modifier.padding(vertical = 8.dp)) {
                state.lastSearches.forEach { search ->
                    SuggestionChip(
                        onClick = {
                            viewModel.onEvent(HomeEvent.OnQueryChange(search))
                            viewModel.onEvent(HomeEvent.OnSearchClick)
                        },
                        label = { Text(search) },
                        modifier = Modifier.padding(end = 8.dp)
                    )
                }
            }
        }

        Divider(modifier = Modifier.padding(vertical = 16.dp))

        // Results
        if (state.stores.isNotEmpty()) {

            Text(
                text = "Nearby Stores",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {
                items(state.stores) { store ->
                    StoreCard(
                        store = store,
                        onClick = {
                            openInMaps(context, store.lat, store.lng, store.name)
                        }
                    )
                }
            }

        } else if (
            !state.isSearching &&
            state.query.isNotEmpty() &&
            state.stores.isEmpty()
        ) {

            val lat = state.latitude
            val lng = state.longitude

            Column(
                modifier = Modifier.fillMaxSize()
            ) {

                Text(
                    "No stores found nearby. Showing your area.",
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(8.dp))

                if (lat != null && lng != null) {
                    NearbyMap(
                        latitude = lat,
                        longitude = lng
                    )
                }

            }
        }

        // Location loading indicator
        if (state.isLoadingLocation) {
            Spacer(modifier = Modifier.height(12.dp))
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            Text(
                "Detecting your location...",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}