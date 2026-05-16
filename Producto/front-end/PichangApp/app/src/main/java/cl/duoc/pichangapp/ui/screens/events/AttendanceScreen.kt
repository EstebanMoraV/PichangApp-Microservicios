package cl.duoc.pichangapp.ui.screens.events

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.ui.graphics.Color
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import cl.duoc.pichangapp.ui.components.PichangButton
import cl.duoc.pichangapp.ui.components.PichangCard
import cl.duoc.pichangapp.ui.components.PichangSnackbar
import cl.duoc.pichangapp.ui.components.LoadingScreen
import cl.duoc.pichangapp.ui.components.EmptyState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttendanceScreen(
    navController: NavController,
    eventId: Int,
    viewModel: EventsViewModel = hiltViewModel()
) {
    val registrations by viewModel.registrations.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(eventId) {
        viewModel.loadRegistrations(eventId)
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Asistencia", fontWeight = FontWeight.Bold) }) },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) { PichangSnackbar(it) } }
    ) { paddingValues ->
        if (isLoading && registrations.isEmpty()) {
            LoadingScreen()
        } else if (registrations.isEmpty()) {
            EmptyState(emoji = "👥", title = "Sin inscritos", message = "Aún no hay nadie inscrito en este evento.")
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    itemsIndexed(registrations, key = { _, item -> item.id }) { index, reg ->
                        AnimatedVisibility(
                            visible = true,
                            enter = fadeIn(tween(300, delayMillis = index * 50)) + slideInHorizontally(tween(300, delayMillis = index * 50))
                        ) {
                            RegistrationRow(
                                reg = reg,
                                eventId = eventId,
                                viewModel = viewModel,
                                snackbarHostState = snackbarHostState
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                PichangButton(
                    onClick = { 
                        scope.launch {
                            val result = viewModel.finishEvent(eventId)
                            if (result.isSuccess) {
                                snackbarHostState.showSnackbar("Evento finalizado correctamente")
                                navController.popBackStack("events", inclusive = false)
                            } else {
                                snackbarHostState.showSnackbar("Error al finalizar")
                            }
                        }
                    },
                    text = "Finalizar Evento",
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
fun RegistrationRow(
    reg: cl.duoc.pichangapp.data.model.EventRegistrationDto,
    eventId: Int,
    viewModel: EventsViewModel,
    snackbarHostState: SnackbarHostState
) {
    var userName by remember { mutableStateOf("Usuario #${reg.userId}") }
    val scope = rememberCoroutineScope()

    LaunchedEffect(reg.userId) {
        userName = viewModel.getUserName(reg.userId)
    }

    PichangCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(userName, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                Spacer(modifier = Modifier.height(4.dp))
                Text("Estado: ${reg.status}", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            
            // Si el estado es REGISTERED, permitir marcar asistencia
            if (reg.status == "REGISTERED") {
                IconButton(
                    onClick = { 
                        scope.launch {
                            val result = viewModel.markAttendance(eventId, reg.userId, true)
                            if (result.isSuccess) {
                                snackbarHostState.showSnackbar("✓ $userName validado como asistente")
                            }
                        }
                    },
                    colors = IconButtonDefaults.iconButtonColors(contentColor = Color(0xFF2E7D32), containerColor = Color(0xFF2E7D32).copy(alpha = 0.1f))
                ) {
                    Icon(Icons.Filled.Check, contentDescription = "Asistió")
                }
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(
                    onClick = { 
                        scope.launch {
                            val result = viewModel.markAttendance(eventId, reg.userId, false)
                            if (result.isSuccess) {
                                snackbarHostState.showSnackbar("✗ $userName marcado como ausente")
                            }
                        }
                    },
                    colors = IconButtonDefaults.iconButtonColors(contentColor = MaterialTheme.colorScheme.error, containerColor = MaterialTheme.colorScheme.errorContainer)
                ) {
                    Icon(Icons.Filled.Close, contentDescription = "No Asistió")
                }
            } else {
                val icon = if (reg.status == "ATTENDED") Icons.Filled.Check else Icons.Filled.Close
                val color = if (reg.status == "ATTENDED") Color(0xFF2E7D32) else MaterialTheme.colorScheme.error
                Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(32.dp).padding(4.dp))
            }
        }
    }
}
