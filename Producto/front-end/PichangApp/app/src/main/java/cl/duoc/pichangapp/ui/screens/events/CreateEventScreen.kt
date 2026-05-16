package cl.duoc.pichangapp.ui.screens.events

import android.location.Geocoder
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import cl.duoc.pichangapp.data.model.CreateEventRequest
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import kotlinx.coroutines.launch
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateEventScreen(
    navController: NavController,
    viewModel: EventsViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    var name by remember { mutableStateOf("") }
    var sport by remember { mutableStateOf("Fútbol") }
    var expandedSport by remember { mutableStateOf(false) }
    var maxPlayers by remember { mutableStateOf("10") }
    var expandedPlayers by remember { mutableStateOf(false) }

    // Date/Time simple input for now (can be enhanced with actual Material3 pickers)
    var date by remember { mutableStateOf("") }
    var time by remember { mutableStateOf("") }
    
    var selectedLocation by remember { mutableStateOf<LatLng?>(null) }
    var locationName by remember { mutableStateOf("Ubicación seleccionada en el mapa") }
    
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(-33.4489, -70.6693), 12f)
    }
    
    val sports = listOf("Fútbol", "Básquetbol", "Tenis", "Vóleibol", "Otro")
    val playersOptions = (1..50).map { it.toString() }

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Crear Partido") }) },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nombre del partido") },
                modifier = Modifier.fillMaxWidth()
            )

            ExposedDropdownMenuBox(
                expanded = expandedSport,
                onExpandedChange = { expandedSport = it }
            ) {
                OutlinedTextField(
                    value = sport,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Deporte") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedSport) },
                    modifier = Modifier.fillMaxWidth().menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = expandedSport,
                    onDismissRequest = { expandedSport = false }
                ) {
                    sports.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                sport = option
                                expandedSport = false
                            }
                        )
                    }
                }
            }

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                OutlinedTextField(
                    value = date,
                    onValueChange = { date = it },
                    label = { Text("Fecha (YYYY-MM-DD)") },
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = time,
                    onValueChange = { time = it },
                    label = { Text("Hora (HH:MM)") },
                    modifier = Modifier.weight(1f)
                )
            }

            ExposedDropdownMenuBox(
                expanded = expandedPlayers,
                onExpandedChange = { expandedPlayers = it }
            ) {
                OutlinedTextField(
                    value = maxPlayers,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Máximo de jugadores") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedPlayers) },
                    modifier = Modifier.fillMaxWidth().menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = expandedPlayers,
                    onDismissRequest = { expandedPlayers = false }
                ) {
                    playersOptions.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                maxPlayers = option
                                expandedPlayers = false
                            }
                        )
                    }
                }
            }

            Text("Ubicación", fontWeight = FontWeight.Bold)
            Box(modifier = Modifier.fillMaxWidth().height(200.dp)) {
                GoogleMap(
                    modifier = Modifier.fillMaxSize(),
                    cameraPositionState = cameraPositionState,
                    onMapClick = { latLng ->
                        selectedLocation = latLng
                        scope.launch {
                            try {
                                val geocoder = Geocoder(context, Locale.getDefault())
                                val addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
                                if (!addresses.isNullOrEmpty()) {
                                    locationName = addresses[0].getAddressLine(0) ?: "Ubicación en el mapa"
                                }
                            } catch (e: Exception) {
                                // Ignore
                            }
                        }
                    }
                ) {
                    selectedLocation?.let {
                        Marker(state = MarkerState(position = it), title = "Ubicación seleccionada")
                    }
                }
            }
            Text("Dirección aproximada: $locationName", style = MaterialTheme.typography.bodySmall)

            Button(
                onClick = {
                    if (name.isBlank() || date.isBlank() || time.isBlank() || selectedLocation == null) {
                        scope.launch { snackbarHostState.showSnackbar("Por favor, completa todos los campos y selecciona ubicación") }
                        return@Button
                    }
                    val eventDateStr = "${date}T${time}:00"
                    
                    viewModel.createEvent(
                        CreateEventRequest(
                            name = name,
                            sport = sport,
                            eventDate = eventDateStr,
                            latitude = selectedLocation!!.latitude,
                            longitude = selectedLocation!!.longitude,
                            locationName = locationName,
                            maxPlayers = maxPlayers.toIntOrNull() ?: 10
                        )
                    )
                    navController.popBackStack()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Crear Partido")
            }
        }
    }
}
