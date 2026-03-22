package com.assignment.zaveproject.ui.home

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.assignment.zaveproject.BuildConfig
import com.assignment.zaveproject.domain.model.Store

@Composable
fun StoreCard(
    store: Store,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .padding(vertical = 8.dp)
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(12.dp)
        ) {
            val imageUrl = store.photoReference?.let {
                "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photo_reference=$it&key=${BuildConfig.MAPS_API_KEY}"
            }

            AsyncImage(
                model = imageUrl,
                contentDescription = store.name,
                modifier = Modifier.size(80.dp),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(store.name, style = MaterialTheme.typography.titleMedium)
                Text(store.address, style = MaterialTheme.typography.bodySmall)
                Text(
                    text = "${store.type.replace("_", " ").capitalize()}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.secondary
                )
                if (store.distance > 0) {
                    Text("${store.distance} km away", style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
//    IconButton(
//        onClick = { viewModel.saveStore(store.id) }
//    ) {
//        Icon(Icons.Default.Favorite, contentDescription = "Save Store")
//    }
//    onClick = {
//        openDirections(context, store.lat, store.lng)
//    }
}

fun openInMaps(context: Context, lat: Double, lng: Double, name: String) {
    val uri = Uri.parse("geo:$lat,$lng?q=$lat,$lng($name)")
    val intent = Intent(Intent.ACTION_VIEW, uri)
    intent.setPackage("com.google.android.apps.maps")
    context.startActivity(intent)
}
fun openDirections(context: Context, lat: Double, lng: Double) {
    val uri = Uri.parse("google.navigation:q=$lat,$lng")
    val intent = Intent(Intent.ACTION_VIEW, uri)
    intent.setPackage("com.google.android.apps.maps")
    context.startActivity(intent)
}
//
//stores.forEach { store ->
//
//    val markerState = rememberMarkerState(
//        position = LatLng(store.lat, store.lng)
//    )
//
//    Marker(
//        state = markerState,
//        title = store.name,
//        snippet = "${store.distance} km away"
//    )
//}
