package cl.duoc.pichangapp.ui.screens.events

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventsScreen(
    navController: NavController? = null,
    initialTab: String = "mis-eventos",
    viewModel: EventsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(initialTab) {
        if (initialTab == "crear-partido") {
            viewModel.setTab(1)
        } else {
            viewModel.setTab(0)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            TabRow(selectedTabIndex = state.selectedTab) {
                Tab(
                    selected = state.selectedTab == 0,
                    onClick = { viewModel.setTab(0) },
                    text = { Text("Mis Eventos") }
                )
                Tab(
                    selected = state.selectedTab == 1,
                    onClick = { viewModel.setTab(1) },
                    text = { Text("Crear Partido") }
                )
            }

            Box(modifier = Modifier.fillMaxSize()) {
                if (state.selectedTab == 0) {
                    MisEventosTab()
                } else {
                    CrearPartidoTab(snackbarHostState)
                }
            }
        }
    }
}

@Composable
fun MisEventosTab() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Aún no tienes eventos",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrearPartidoTab(snackbarHostState: SnackbarHostState) {
    var nombre by remember { mutableStateOf("") }
    var deporte by remember { mutableStateOf("Fútbol") }
    var expanded by remember { mutableStateOf(false) }
    var ubicacion by remember { mutableStateOf("") }
    var maxJugadores by remember { mutableStateOf("") }

    // Dummy values for date/time since we are requested to just show fields
    // Real implementation would use DatePickerDialog and TimePickerDialog
    var fecha by remember { mutableStateOf("") }
    var hora by remember { mutableStateOf("") }

    val deportes = listOf("Fútbol", "Básquetbol", "Tenis", "Vóleibol", "Otro")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Nuevo Partido",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre del partido") },
            modifier = Modifier.fillMaxWidth()
        )

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = it }
        ) {
            OutlinedTextField(
                value = deporte,
                onValueChange = {},
                readOnly = true,
                label = { Text("Deporte") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                deportes.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            deporte = option
                            expanded = false
                        }
                    )
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = fecha,
                onValueChange = { fecha = it },
                label = { Text("Fecha (DD/MM/YYYY)") },
                modifier = Modifier.weight(1f)
            )
            OutlinedTextField(
                value = hora,
                onValueChange = { hora = it },
                label = { Text("Hora (HH:MM)") },
                modifier = Modifier.weight(1f)
            )
        }

        OutlinedTextField(
            value = ubicacion,
            onValueChange = { ubicacion = it },
            label = { Text("Ubicación") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = maxJugadores,
            onValueChange = { maxJugadores = it },
            label = { Text("Máximo de jugadores") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        Spacer(modifier = Modifier.height(16.dp))

        LaunchedEffect(Unit) {
            // Keep this scope available for snackbar
        }

        val scope = rememberCoroutineScope()

        Button(
            onClick = {
                scope.launch {
                    snackbarHostState.showSnackbar("Funcionalidad próximamente disponible")
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Crear")
        }
    }
}
