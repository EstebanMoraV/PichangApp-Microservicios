package cl.duoc.pichangapp.ui.screens.events

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventDetailScreen(
    navController: NavController,
    eventId: Int,
    viewModel: EventsViewModel = hiltViewModel()
) {
    val event by viewModel.eventDetail.collectAsState()
    val userIdStr by viewModel.currentUserId.collectAsState(initial = null)
    val userId = userIdStr?.toIntOrNull()

    LaunchedEffect(eventId) {
        viewModel.loadEventDetail(eventId)
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Detalle del Evento") })
        }
    ) { paddingValues ->
        if (event == null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            val e = event!!
            val isOrganizer = e.organizerId == userId
            
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Box(modifier = Modifier.fillMaxWidth().height(200.dp)) {
                    val latLng = LatLng(e.latitude, e.longitude)
                    val cameraPositionState = rememberCameraPositionState {
                        position = CameraPosition.fromLatLngZoom(latLng, 14f)
                    }
                    GoogleMap(
                        modifier = Modifier.fillMaxSize(),
                        cameraPositionState = cameraPositionState,
                        uiSettings = com.google.maps.android.compose.MapUiSettings(zoomControlsEnabled = false)
                    ) {
                        Marker(state = MarkerState(position = latLng), title = e.name)
                    }
                }

                Text(e.name, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                Text("${e.sport} • ${e.eventDate}", style = MaterialTheme.typography.titleMedium)
                
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.LocationOn, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(e.locationName, style = MaterialTheme.typography.bodyLarge)
                }
                
                if (e.distanceKm != null) {
                    Text("A ${String.format("%.1f", e.distanceKm)} km de ti", style = MaterialTheme.typography.bodyMedium)
                }

                Spacer(modifier = Modifier.height(8.dp))
                
                Text("Jugadores: ${e.currentPlayers} / ${e.maxPlayers}")
                LinearProgressIndicator(
                    progress = if (e.maxPlayers > 0) e.currentPlayers.toFloat() / e.maxPlayers else 0f,
                    modifier = Modifier.fillMaxWidth().height(8.dp)
                )

                Spacer(modifier = Modifier.weight(1f))

                if (isOrganizer) {
                    Button(
                        onClick = { navController.navigate("events/${e.id}/attendance") },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Ver inscritos")
                    }
                    OutlinedButton(
                        onClick = { 
                            viewModel.finishEvent(e.id)
                            navController.popBackStack()
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Finalizar evento")
                    }
                } else {
                    Button(
                        onClick = { viewModel.joinEvent(e.id) },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = e.currentPlayers < e.maxPlayers
                    ) {
                        Text(if (e.currentPlayers < e.maxPlayers) "Unirse al partido" else "Cupos llenos")
                    }
                    OutlinedButton(
                        onClick = { 
                            // TODO: Add actual location checking
                            viewModel.checkIn(e.id, e.latitude, e.longitude) 
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Check-in (Geolocalizado)")
                    }
                }
            }
        }
    }
}
