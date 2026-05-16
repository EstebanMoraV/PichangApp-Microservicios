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
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventDetailScreen(
    navController: NavController,
    eventId: Int,
    viewModel: EventsViewModel = hiltViewModel()
) {
    val event by viewModel.eventDetail.collectAsState()
    val myEvents by viewModel.myEvents.collectAsState()
    val userIdStr by viewModel.currentUserId.collectAsState(initial = null)
    val userId = userIdStr?.toIntOrNull()

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var showCancelDialog by remember { mutableStateOf(false) }

    LaunchedEffect(eventId) {
        viewModel.loadEventDetail(eventId)
        viewModel.loadMyEvents()
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Detalle del Evento") })
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        if (event == null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            val e = event!!
            val isOrganizer = e.organizerId == userId
            val isRegistered = myEvents.any { it.id == eventId }
            
            if (showCancelDialog) {
                AlertDialog(
                    onDismissRequest = { showCancelDialog = false },
                    title = { Text("Cancelar participación") },
                    text = { Text("¿Estás seguro que deseas cancelar tu participación?") },
                    confirmButton = {
                        TextButton(onClick = {
                            showCancelDialog = false
                            scope.launch {
                                val result = viewModel.leaveEvent(e.id)
                                if (result.isSuccess) {
                                    snackbarHostState.showSnackbar("Participación cancelada")
                                } else {
                                    val msg = result.exceptionOrNull()?.message ?: "Error al cancelar"
                                    snackbarHostState.showSnackbar(msg)
                                }
                            }
                        }) { Text("Confirmar", color = MaterialTheme.colorScheme.error) }
                    },
                    dismissButton = {
                        TextButton(onClick = { showCancelDialog = false }) { Text("Volver") }
                    }
                )
            }

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
                            scope.launch {
                                val result = viewModel.finishEvent(e.id)
                                if (result.isSuccess) {
                                    snackbarHostState.showSnackbar("Evento finalizado correctamente")
                                    navController.popBackStack()
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Finalizar evento")
                    }
                } else {
                    if (isRegistered) {
                        Button(
                            onClick = { showCancelDialog = true },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                        ) {
                            Text("Cancelar participación")
                        }
                        OutlinedButton(
                            onClick = { 
                                scope.launch {
                                    val result = viewModel.checkIn(e.id, e.latitude, e.longitude)
                                    if (result.isSuccess) {
                                        snackbarHostState.showSnackbar("¡Check-in realizado! +10 karma")
                                    } else {
                                        val msg = result.exceptionOrNull()?.message ?: "Error al hacer check-in"
                                        snackbarHostState.showSnackbar(msg)
                                    }
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Check-in (Geolocalizado)")
                        }
                    } else {
                        Button(
                            onClick = { 
                                scope.launch {
                                    val result = viewModel.joinEvent(e.id)
                                    if (result.isSuccess) {
                                        snackbarHostState.showSnackbar("¡Te uniste al evento!")
                                    } else {
                                        val msg = result.exceptionOrNull()?.message ?: "Error al unirse"
                                        snackbarHostState.showSnackbar(msg)
                                    }
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = e.currentPlayers < e.maxPlayers
                        ) {
                            Text(if (e.currentPlayers < e.maxPlayers) "Unirse al partido" else "Cupos llenos")
                        }
                    }
                }
            }
        }
    }
}
