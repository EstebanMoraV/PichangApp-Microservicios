package cl.duoc.pichangapp.ui.screens.events

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.android.gms.location.LocationServices
import com.google.maps.android.compose.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventsScreen(
    navController: NavController,
    viewModel: EventsViewModel = hiltViewModel()
) {
    val events by viewModel.events.collectAsState()
    val myEvents by viewModel.myEvents.collectAsState()
    val organizingEvents by viewModel.organizingEvents.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val context = LocalContext.current
    var hasLocationPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        )
    }
    
    var selectedTab by remember { mutableStateOf(0) }
    var userLat by remember { mutableStateOf<Double?>(null) }
    var userLng by remember { mutableStateOf<Double?>(null) }

    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        hasLocationPermission = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true || 
                              permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
    }

    LaunchedEffect(hasLocationPermission) {
        if (hasLocationPermission) {
            try {
                fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                    if (location != null) {
                        userLat = location.latitude
                        userLng = location.longitude
                        viewModel.loadEvents(location.latitude, location.longitude)
                    } else {
                        // Fallback location or prompt GPS
                        viewModel.loadEvents(-33.4489, -70.6693) // Santiago default
                    }
                }
            } catch (e: SecurityException) {
                // Handled
            }
        } else {
            permissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
        
        viewModel.loadMyEvents()
        viewModel.loadOrganizingEvents()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Eventos") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            TabRow(selectedTabIndex = selectedTab) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("Buscar Eventos") }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("Mis Eventos") }
                )
            }

            if (selectedTab == 0) {
                if (!hasLocationPermission) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Se requiere permiso de ubicación para buscar eventos cercanos.")
                    }
                } else if (userLat != null && userLng != null) {
                    BuscarEventosTab(events, userLat!!, userLng!!, navController)
                } else {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
            } else {
                MisEventosTab(myEvents, organizingEvents, navController)
            }
        }
    }
}

@Composable
fun BuscarEventosTab(events: List<cl.duoc.pichangapp.data.model.EventDto>, lat: Double, lng: Double, navController: NavController) {
    val cameraPositionState = rememberCameraPositionState {
        position = com.google.android.gms.maps.model.CameraPosition.fromLatLngZoom(com.google.android.gms.maps.model.LatLng(lat, lng), 12f)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.weight(1f)) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                properties = MapProperties(isMyLocationEnabled = true)
            ) {
                events.forEach { event ->
                    Marker(
                        state = MarkerState(position = com.google.android.gms.maps.model.LatLng(event.latitude, event.longitude)),
                        title = event.name,
                        snippet = event.sport
                    )
                }
            }
        }
        
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(events.sortedBy { it.distanceKm ?: 0.0 }) { event ->
                EventCard(event = event, onClick = { navController.navigate("events/${event.id}") })
            }
        }
    }
}

@Composable
fun MisEventosTab(myEvents: List<cl.duoc.pichangapp.data.model.EventDto>, organizingEvents: List<cl.duoc.pichangapp.data.model.EventDto>, navController: NavController) {
    var subTab by remember { mutableStateOf(0) }
    
    Column(modifier = Modifier.fillMaxSize()) {
        TabRow(selectedTabIndex = subTab) {
            Tab(selected = subTab == 0, onClick = { subTab = 0 }, text = { Text("Inscrito") })
            Tab(selected = subTab == 1, onClick = { subTab = 1 }, text = { Text("Organizando") })
        }
        
        val list = if (subTab == 0) myEvents else organizingEvents
        
        if (list.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No hay eventos en esta categoría.")
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(list) { event ->
                    EventCard(event = event, onClick = { navController.navigate("events/${event.id}") })
                }
            }
        }
    }
}

@Composable
fun EventCard(event: cl.duoc.pichangapp.data.model.EventDto, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(event.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text("${event.sport} • ${event.eventDate}")
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.LocationOn, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(event.locationName, style = MaterialTheme.typography.bodyMedium)
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Jugadores: ${event.currentPlayers}/${event.maxPlayers}")
                if (event.distanceKm != null) {
                    Text(String.format("%.1f km", event.distanceKm))
                }
            }
        }
    }
}
